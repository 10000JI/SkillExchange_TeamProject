package place.skillexchange.backend.dto;

import lombok.Getter;
import place.skillexchange.backend.entity.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceDto {

    /**
     * 장소 목록(조회) 성공시 보낼 Dto
     */
    @Getter
    public static class ReadResponse {
        private List<String> placeName = new ArrayList<>();

        /* Entity -> Dto */
        public ReadResponse(List<Place> place) {
            for (Place p : place) {
                this.placeName.add(p.getPlaceName());
            }
        }
    }
}
