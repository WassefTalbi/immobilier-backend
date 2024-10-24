package OneWayDev.tn.OneWayDev.Service;

import OneWayDev.tn.OneWayDev.Repository.PropertyRepository;
import OneWayDev.tn.OneWayDev.Repository.UserRepository;
import OneWayDev.tn.OneWayDev.dto.request.PropertyDTO;
import OneWayDev.tn.OneWayDev.entity.Property;
import OneWayDev.tn.OneWayDev.entity.User;
import OneWayDev.tn.OneWayDev.exception.NotFoundExecption;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final FileService fileService;


    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id).orElseThrow(()->new NotFoundExecption("property not found"));
    }

    public Property saveProperty(PropertyDTO propertyDTO, Authentication authentication) {
       User angency= userRepository.findByEmail(authentication.getName()).orElseThrow(()->new NotFoundExecption("agency not found"));
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
        property.setStreetAddress(propertyDTO.getStreetAddress());
        property.setState(propertyDTO.getState());
        property.setCountry(propertyDTO.getCountry());
        property.setZipcode(propertyDTO.getZipcode());
        property.setAdditionalFeatures(propertyDTO.getAdditionalFeatures());
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
        property.setStreetAddress(propertyDTO.getStreetAddress());
        property.setState(propertyDTO.getState());
        property.setCountry(propertyDTO.getCountry());
        property.setZipcode(propertyDTO.getZipcode());
        property.setAdditionalFeatures(propertyDTO.getAdditionalFeatures());
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


}
