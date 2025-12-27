package br.com.fiap.report.rating.repository;

import br.com.fiap.report.rating.entity.RatingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RatingRepository extends MongoRepository<RatingEntity, String> {

    List<RatingEntity> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    Long countByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    Long countByDateTimeBetweenAndIsCritical(LocalDateTime start, LocalDateTime end, Boolean isCritical);

}
