package br.com.fiap.report.rating.dto.request;

public record NewRatingDTO(
        String description,
        Integer rating,
        Boolean isCritical
) { }
