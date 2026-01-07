package br.com.fiap.report.rating.dto;

import java.time.LocalDateTime;
import java.util.List;

public record RatingReportDTO(
        String dateTimeEmission,
        List<RatingReportDataDTO> ratingCountByDate,
        List<RatingReportDataDTO> ratingCountByUrgency
) { }
