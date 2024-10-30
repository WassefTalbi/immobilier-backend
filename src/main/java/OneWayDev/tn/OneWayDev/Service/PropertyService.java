package OneWayDev.tn.OneWayDev.Service;

import OneWayDev.tn.OneWayDev.Repository.FeatureRepository;
import OneWayDev.tn.OneWayDev.Repository.PropertyRepository;
import OneWayDev.tn.OneWayDev.Repository.UserRepository;
import OneWayDev.tn.OneWayDev.dto.request.PropertyDTO;
import OneWayDev.tn.OneWayDev.entity.Feature;
import OneWayDev.tn.OneWayDev.entity.FeatureType;
import OneWayDev.tn.OneWayDev.entity.Property;
import OneWayDev.tn.OneWayDev.entity.User;
import OneWayDev.tn.OneWayDev.exception.NotFoundExecption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final FeatureRepository featureRepository;
    private final FileService fileService;


    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }
    public List<Property> getPropertiesByAgency(String email) {
        return propertyRepository.findByAngencyEmail(email);
    }
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id).orElseThrow(()->new NotFoundExecption("property not found"));
    }

    public Property saveProperty(PropertyDTO propertyDTO, String email) {
       User angency= userRepository.findByEmail(email).orElseThrow(()->new NotFoundExecption("agency not found"));
        Property property = new Property();
        property.setTitle(propertyDTO.getTitle());
        property.setType(propertyDTO.getType());
        property.setBedroom(propertyDTO.getBedroom());
        property.setBathroom(propertyDTO.getBathroom());
        property.setArea(propertyDTO.getArea());
        property.setPrice(propertyDTO.getPrice());
        property.setAngency(angency);
        property.setRequirement(propertyDTO.getRequirement());
        property.setLocation(propertyDTO.getLocation());
       List<Feature> features = propertyDTO.getAdditionalFeatures().stream()
                .map(feature -> featureRepository.findByFeatureName(FeatureType.valueOf(feature))
                        .orElseThrow(() -> new NotFoundExecption("Feature not found: " + feature)))
                .collect(Collectors.toList());
        property.setFeatures(features);
        String img= fileService.uploadFile(propertyDTO.getImage());
        property.setImage(img);
        return propertyRepository.save(property);
    }


    public Property updateProperty(Long id, PropertyDTO propertyDTO) {
        Property property = propertyRepository.findById(id).orElseThrow(()->new NotFoundExecption("property not found"));
        property.setTitle(propertyDTO.getTitle());
        property.setType(propertyDTO.getType());
        property.setBedroom(propertyDTO.getBedroom());
        property.setBathroom(propertyDTO.getBathroom());
        property.setArea(propertyDTO.getArea());
        property.setPrice(propertyDTO.getPrice());
        property.setRequirement(propertyDTO.getRequirement());
        property.setLocation(propertyDTO.getLocation());
        List<Feature> features = propertyDTO.getAdditionalFeatures().stream()
                .map(featureName -> featureRepository.findByFeatureName(FeatureType.valueOf(featureName))
                        .orElseThrow(() -> new NotFoundExecption("Feature not found: " + featureName)))
                .collect(Collectors.toList());
        property.setFeatures(features);
        if (propertyDTO.getImage() != null && !propertyDTO.getImage().isEmpty()) {
            String img= fileService.uploadFile(propertyDTO.getImage());
            property.setImage(img);
        }
        return propertyRepository.save(property);
    }

    public void deleteProperty(Long id) {
        propertyRepository.findById(id).orElseThrow(()->new NotFoundExecption("property not found"));
        propertyRepository.deleteById(id);
    }

    public void ratingProperty(){
        //traitement of rating a property
    }

}
