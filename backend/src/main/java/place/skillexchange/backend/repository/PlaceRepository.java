package place.skillexchange.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import place.skillexchange.backend.entity.Place;

public interface PlaceRepository  extends JpaRepository<Place, Long> {
}
