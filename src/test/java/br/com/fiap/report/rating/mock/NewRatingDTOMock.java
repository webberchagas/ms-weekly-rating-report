package br.com.fiap.report.rating.mock;

import br.com.fiap.report.rating.dto.request.NewRatingDTO;

public class NewRatingDTOMock {

    public static NewRatingDTO getNewRatingDTO() {
        return new NewRatingDTO(
                "Description",
                5,
                false
        );
    }

}
