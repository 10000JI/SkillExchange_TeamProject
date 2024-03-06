package place.skillexchange.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import place.skillexchange.backend.dto.PlaceDto;
import place.skillexchange.backend.repository.PlaceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceDto.ReadResponse list() {
        return new PlaceDto.ReadResponse(placeRepository.findAll());
    }
}
