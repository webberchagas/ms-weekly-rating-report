package br.com.fiap.report.rating.service.impl;

import br.com.fiap.report.rating.dto.queue.RatingReportDTO;
import br.com.fiap.report.rating.dto.queue.RatingReportData;
import br.com.fiap.report.rating.dto.request.NewRatingDTO;
import br.com.fiap.report.rating.dto.response.RatingCountByDateDTO;
import br.com.fiap.report.rating.entity.RatingEntity;
import br.com.fiap.report.rating.repository.RatingRepository;
import br.com.fiap.report.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final MongoTemplate mongoTemplate;

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

    @Override
    public List<RatingCountByDateDTO> countRatingByDateTimeBetweenGroupedByDate(LocalDateTime start, LocalDateTime end) {
        MatchOperation matchOperation = Aggregation.match(
                Criteria.where("dateTime").gte(start).lte(end)
        );

        ProjectionOperation projectFormattedDate = Aggregation.project()
                .and(
                        DateOperators
                                .dateOf("dateTime")
                                .toString("%Y-%m-%d")
                ).as("date");

        GroupOperation groupOperation = Aggregation.group("date")
                .count().as("total");

        ProjectionOperation finalProjection = Aggregation.project()
                .and("_id").as("date")
                .and("total").as("total");

        SortOperation sortOperation = Aggregation.sort(Sort.Direction.ASC, "_id");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                projectFormattedDate,
                groupOperation,
                finalProjection,
                sortOperation
        );

        AggregationResults<RatingCountByDateDTO> ratingCountByDateDTOAggregationResults = mongoTemplate.aggregate(
                aggregation,
                "ratings",
                RatingCountByDateDTO.class
        );

        return ratingCountByDateDTOAggregationResults.getMappedResults();
    }

    @Override
    public RatingReportDTO getRatingReportByDateTimeBetween(LocalDateTime start, LocalDateTime end) {
        List<RatingReportData> ratingsCountByDate = countRatingByDateTimeBetweenGroupedByDate(start, end)
                .stream()
                .map(RatingReportData::new)
                .toList();

        List<RatingReportData> ratingsCountByUrgency = countRatingByDateTimeBetweenGroupedByIsCritical(start, end)
                .entrySet()
                .stream()
                .map(RatingReportData::new)
                .toList();

        return new RatingReportDTO(
                ratingsCountByDate,
                ratingsCountByUrgency,
                LocalDateTime.now()
        );
    }

}
