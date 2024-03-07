package place.skillexchange.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import place.skillexchange.backend.entity.Place;

import java.util.Optional;

public interface PlaceRepository  extends JpaRepository<Place, Long> {

    Optional<Place> findByPlaceName(String placeName);
}
