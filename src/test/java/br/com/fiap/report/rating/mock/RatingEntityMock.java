package br.com.fiap.report.rating.mock;

import br.com.fiap.report.rating.entity.RatingEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RatingEntityMock {

    public static RatingEntity getRatingEntity() {
        return new RatingEntity(
                UUID.randomUUID().toString(),
                "Description",
                5,
                false,
                LocalDateTime.now()
        );
    }

    public static List<RatingEntity> getRatingEntityList() {
        return List.of(
                getRatingEntity(),
                getRatingEntity(),
                getRatingEntity(),
                getRatingEntity(),
                getRatingEntity()
        );
    }

}
