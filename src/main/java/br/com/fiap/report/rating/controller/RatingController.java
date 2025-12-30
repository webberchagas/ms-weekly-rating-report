package br.com.fiap.report.rating.controller;

import br.com.fiap.report.rating.dto.queue.RatingReportDTO;
import br.com.fiap.report.rating.dto.response.RatingCountByDateDTO;
import br.com.fiap.report.rating.entity.RatingEntity;
import br.com.fiap.report.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public List<RatingEntity> listAllRatings() {
        return ratingService.getAllRatingByDateTimeBetween(LocalDateTime.now().minusDays(7), LocalDateTime.now());
    }

    @GetMapping("/count")
    public Long countRatings() {
        return ratingService.countRatingByDateTimeBetween(LocalDateTime.now().minusDays(7), LocalDateTime.now());
    }

    @GetMapping("/count/grouped-by-critical")
    public Map<String, Long> countRatingsGroupedByCritical() {
        return ratingService.countRatingByDateTimeBetweenGroupedByIsCritical(LocalDateTime.now().minusDays(7), LocalDateTime.now());
    }

    @GetMapping("/count/grouped-by-date")
    public List<RatingCountByDateDTO> countRatingsGroupedByDate() {
        return ratingService.countRatingByDateTimeBetweenGroupedByDate(LocalDateTime.now().minusDays(7), LocalDateTime.now());
    }

    @GetMapping("/weekly-rating-report")
    public RatingReportDTO weeklyRatingReport() {
        return ratingService.getRatingReportByDateTimeBetween(LocalDateTime.now().minusDays(7), LocalDateTime.now());
    }

}
