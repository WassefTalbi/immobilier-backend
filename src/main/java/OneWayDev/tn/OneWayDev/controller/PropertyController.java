package OneWayDev.tn.OneWayDev.controller;

import OneWayDev.tn.OneWayDev.Service.PropertyService;
import OneWayDev.tn.OneWayDev.dto.request.PropertyDTO;
import OneWayDev.tn.OneWayDev.entity.Property;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;



    @GetMapping("/all")
    public ResponseEntity<?> getAllProperties() {
        List<Property> properties = propertyService.getAllProperties();
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> getPropertyById(@PathVariable Long id) {
        return new ResponseEntity<>(propertyService.getPropertyById(id), HttpStatus.OK);

    }

    @PostMapping("/add")
    public ResponseEntity<?> saveProperty(@ModelAttribute PropertyDTO propertyDTO, @AuthenticationPrincipal Authentication authenticationPrincipal) {

            return new ResponseEntity<>(propertyService.saveProperty(propertyDTO,authenticationPrincipal), HttpStatus.CREATED);
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProperty(@PathVariable Long id, @ModelAttribute PropertyDTO propertyDTO) {
        Property updatedProperty = propertyService.updateProperty(id, propertyDTO);
        if (updatedProperty != null) {
            return new ResponseEntity<>(updatedProperty, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Property not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>  deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}