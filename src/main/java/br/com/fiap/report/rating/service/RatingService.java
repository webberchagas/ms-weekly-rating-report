package br.com.fiap.report.rating.service;

import br.com.fiap.report.rating.dto.queue.RatingReportDTO;
import br.com.fiap.report.rating.dto.request.NewRatingDTO;
import br.com.fiap.report.rating.dto.response.RatingCountByDateDTO;
import br.com.fiap.report.rating.entity.RatingEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RatingService {

    RatingEntity addNewRating(NewRatingDTO newRatingDTO);
    List<RatingEntity> getAllRatingByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    Long countRatingByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    Map<String, Long> countRatingByDateTimeBetweenGroupedByIsCritical(LocalDateTime start, LocalDateTime end);
    List<RatingCountByDateDTO> countRatingByDateTimeBetweenGroupedByDate(LocalDateTime start, LocalDateTime end);
    RatingReportDTO getRatingReportByDateTimeBetween(LocalDateTime start, LocalDateTime end);

}
