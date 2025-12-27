package br.com.fiap.report.rating.service.impl;

import br.com.fiap.report.rating.dto.request.NewRatingDTO;
import br.com.fiap.report.rating.entity.RatingEntity;
import br.com.fiap.report.rating.repository.RatingRepository;
import br.com.fiap.report.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Override
    public RatingEntity addNewRating(NewRatingDTO newRatingDTO) {
        RatingEntity ratingEntity = new RatingEntity(newRatingDTO);

        return ratingRepository.save(ratingEntity);
    }

    @Override
    public List<RatingEntity> getAllRatingByDateTimeBetween(LocalDateTime start, LocalDateTime end) {
        return ratingRepository.findByDateTimeBetween(start, end);
    }

    @Override
    public Long countRatingByDateTimeBetween(LocalDateTime start, LocalDateTime end) {
        return ratingRepository.countByDateTimeBetween(start, end);
    }

    @Override
    public Map<String, Long> countRatingByDateTimeBetweenGroupedByIsCritical(LocalDateTime start, LocalDateTime end) {
        Map<String, Long> ratingCountGroupedByIsCritical = new HashMap<>();

        ratingCountGroupedByIsCritical.put(
                "CRITICAL",
                ratingRepository.countByDateTimeBetweenAndIsCritical(start, end, true)
        );

        ratingCountGroupedByIsCritical.put(
                "NORMAL",
                ratingRepository.countByDateTimeBetweenAndIsCritical(start, end, false)
        );

        return ratingCountGroupedByIsCritical;
    }

}
