package br.com.fiap.report.rating.dto.queue;

import java.time.LocalDateTime;
import java.util.List;

public record RatingReportDTO(
        List<RatingReportData> ratingCountByDate,
        List<RatingReportData> ratingCountByUrgency,
        LocalDateTime dateTimeEmission
) {
}
