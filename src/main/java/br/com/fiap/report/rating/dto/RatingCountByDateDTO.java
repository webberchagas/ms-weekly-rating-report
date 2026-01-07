package br.com.fiap.report.rating.dto;

public record RatingCountByDateDTO(
        String date,
        Long total
) { }
