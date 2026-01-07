package br.com.fiap.report.rating.service;

import br.com.fiap.report.rating.dto.RatingReportDTO;
import br.com.fiap.report.rating.dto.RatingReportDataDTO;
import br.com.fiap.report.rating.repository.RatingRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingService {

    private final RatingRepository ratingRepository = new RatingRepository();
    private static final DateTimeFormatter ISO_UTC = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd'T'HH:mm:ss'Z'"
    );

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

    public RatingReportDTO generateRatingReportDTO(LocalDateTime start, LocalDateTime end) {
        List<RatingReportDataDTO> ratingCountByDate = ratingRepository.countRatingByDateTimeBetweenGroupedByDate(
                start,
                end
        ).stream().map(ratingByDate -> new RatingReportDataDTO(
                ratingByDate.date(),
                ratingByDate.total()
        )).toList();

        List<RatingReportDataDTO> ratingCountByUrgency = countRatingByDateTimeBetweenGroupedByIsCritical(start, end)
                .entrySet()
                .stream()
                .map(values -> new RatingReportDataDTO(
                        values.getKey(),
                        values.getValue()
                ))
                .toList();


        return new RatingReportDTO(
                LocalDateTime.now().atZone(ZoneOffset.UTC).format(ISO_UTC),
                ratingCountByDate,
                ratingCountByUrgency
        );
    }

}
