package place.skillexchange.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import place.skillexchange.backend.dto.PlaceDto;
import place.skillexchange.backend.repository.PlaceRepository;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceDto.PlaceReadResponse list() {
        return new PlaceDto.PlaceReadResponse(placeRepository.findAll());
    }
}
