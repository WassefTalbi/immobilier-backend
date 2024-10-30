package OneWayDev.tn.OneWayDev.Repository;

import OneWayDev.tn.OneWayDev.entity.Property;
import OneWayDev.tn.OneWayDev.entity.Rating;
import OneWayDev.tn.OneWayDev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface RatingRepository extends JpaRepository<Rating,Long> {
    List<Rating> findByPropertyId(Long propertyId);
    Optional<Rating> findByUserAndProperty(User user, Property property);


}
