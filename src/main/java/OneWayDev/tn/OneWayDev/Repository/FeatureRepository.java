package OneWayDev.tn.OneWayDev.Repository;

import OneWayDev.tn.OneWayDev.entity.Feature;
import OneWayDev.tn.OneWayDev.entity.FeatureType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeatureRepository extends JpaRepository<Feature,Long> {
    Optional<Feature> findByFeatureName(FeatureType featureType);

}
