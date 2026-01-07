package br.com.fiap.report.rating.entity;

import java.time.LocalDateTime;

public class RatingEntity {

    private String id;
    private String description;
    private Integer rating;
    private Boolean isCritical;
    private LocalDateTime dateTime;

    public RatingEntity(String id, String description, Integer rating, Boolean isCritical, LocalDateTime dateTime) {
        this.id = id;
        this.description = description;
        this.rating = rating;
        this.isCritical = isCritical;
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Boolean getCritical() {
        return isCritical;
    }

    public Integer getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

}
