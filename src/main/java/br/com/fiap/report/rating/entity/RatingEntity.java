package br.com.fiap.report.rating.entity;

import br.com.fiap.report.rating.dto.request.NewRatingDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document(collection = "ratings")
public class RatingEntity {

    @Id
    private String id;
    private String description;
    private Integer rating;
    private Boolean isCritical;
    private LocalDateTime dateTime;

    public RatingEntity(NewRatingDTO newRatingDTO) {
        this.description = newRatingDTO.description();
        this.rating = newRatingDTO.rating();
        this.isCritical = newRatingDTO.isCritical();
        this.dateTime = LocalDateTime.now();
    }

}
