package OneWayDev.tn.OneWayDev.Repository;

import OneWayDev.tn.OneWayDev.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PropertyRepository extends JpaRepository<Property,Long> {
List<Property>findByAngencyEmail(String email);
}
