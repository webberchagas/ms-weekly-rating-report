package br.com.fiap.report.rating.dto.response;

public record RatingCountByDateDTO(
        String date,
        Integer total
) {
}
