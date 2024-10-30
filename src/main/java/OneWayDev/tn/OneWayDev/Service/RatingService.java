package OneWayDev.tn.OneWayDev.Service;

import OneWayDev.tn.OneWayDev.Repository.FeatureRepository;
import OneWayDev.tn.OneWayDev.Repository.PropertyRepository;
import OneWayDev.tn.OneWayDev.Repository.RatingRepository;
import OneWayDev.tn.OneWayDev.Repository.UserRepository;
import OneWayDev.tn.OneWayDev.dto.request.PropertyDTO;
import OneWayDev.tn.OneWayDev.entity.*;
import OneWayDev.tn.OneWayDev.exception.NotFoundExecption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RatingService {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    public Rating rateProperty(Long userId, Long propertyId, Integer score) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        Rating rating = ratingRepository.findByUserAndProperty(user, property)
                .orElse(new Rating());

        rating.setUser(user);
        rating.setProperty(property);
        rating.setScore(score);
        Double avg=getAverageRatingForProperty(propertyId);
        property.setAverageRating(avg);
        propertyRepository.save(property);
        return ratingRepository.save(rating);
    }

    public Double getAverageRatingForProperty(Long propertyId) {
        List<Rating> ratings = ratingRepository.findByPropertyId(propertyId);
        return ratings.stream()
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0.0);
    }


}
