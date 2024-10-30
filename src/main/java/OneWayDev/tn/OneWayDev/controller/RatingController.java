package OneWayDev.tn.OneWayDev.controller;

import OneWayDev.tn.OneWayDev.Repository.PropertyRepository;
import OneWayDev.tn.OneWayDev.Service.PropertyService;
import OneWayDev.tn.OneWayDev.Service.RatingService;
import OneWayDev.tn.OneWayDev.Service.UserService;
import OneWayDev.tn.OneWayDev.dto.request.PropertyDTO;
import OneWayDev.tn.OneWayDev.dto.request.RatePropertyRequest;
import OneWayDev.tn.OneWayDev.entity.Property;
import OneWayDev.tn.OneWayDev.entity.Rating;
import OneWayDev.tn.OneWayDev.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rating")
@CrossOrigin("*")
@RequiredArgsConstructor
public class RatingController {
    private final PropertyRepository propertyRepository;
    private final RatingService ratingService;
    @PostMapping("/rate")
    public Rating rateProperty(@RequestBody RatePropertyRequest request) {
        return ratingService.rateProperty(request.getUserId(), request.getPropertyId(), request.getScore());
    }

    @GetMapping("/average")
    public Property getPropertyWithAverageRating(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        Double averageRating = ratingService.getAverageRatingForProperty(propertyId);
        property.setAverageRating(averageRating);
        return property;
    }

}
