package br.com.fiap.report.rating.dto.queue;

import br.com.fiap.report.rating.dto.response.RatingCountByDateDTO;

import java.util.Map;

public record RatingReportData(
        String label,
        Integer value
) {

    public RatingReportData(RatingCountByDateDTO ratingCountByDateDTO) {
        this(
                ratingCountByDateDTO.date(),
                ratingCountByDateDTO.total()
        );
    }

    public RatingReportData(Map.Entry<String, Long> stringLongEntry) {
        this(
                stringLongEntry.getKey(),
                stringLongEntry.getValue().intValue()
        );
    }
}
