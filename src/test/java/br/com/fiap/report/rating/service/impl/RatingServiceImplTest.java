package br.com.fiap.report.rating.service.impl;

import br.com.fiap.report.rating.dto.request.NewRatingDTO;
import br.com.fiap.report.rating.entity.RatingEntity;
import br.com.fiap.report.rating.mock.NewRatingDTOMock;
import br.com.fiap.report.rating.mock.RatingEntityMock;
import br.com.fiap.report.rating.repository.RatingRepository;
import br.com.fiap.report.rating.service.RatingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RatingServiceImplTest {

    private AutoCloseable mock;

    @Mock
    private RatingRepository ratingRepository;
    private RatingService ratingService;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        ratingService = new RatingServiceImpl(ratingRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void shoulgBeCreateNewRating() {
        RatingEntity createdRating = RatingEntityMock.getRatingEntity();
        NewRatingDTO newRatingDTO = NewRatingDTOMock.getNewRatingDTO();
        when(ratingRepository.save(any(RatingEntity.class))).thenReturn(createdRating);

        RatingEntity returnedRating = ratingService.addNewRating(newRatingDTO);

        assertEquals(createdRating, returnedRating);
        verify(ratingRepository, times(1)).save(any(RatingEntity.class));
    }

    @Test
    void shouldBeReturnRatingEntityListByDateTimeBetween() {
        List<RatingEntity> ratingEntityList = RatingEntityMock.getRatingEntityList();
        when(ratingRepository.findByDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(ratingEntityList);

        List<RatingEntity> returnedRatingEntityList = ratingService.getAllRatingByDateTimeBetween(
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now()
        );

        assertEquals(ratingEntityList, returnedRatingEntityList);
        verify(ratingRepository, times(1))
                .findByDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void shouldBeReturnRatingEntityCountByDateTimeBetween() {
        Long ratingEntityCount = 20L;
        when(ratingRepository.countByDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(ratingEntityCount);

        Long returnedRatingEntityCount = ratingService.countRatingByDateTimeBetween(
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now()
        );

        assertEquals(ratingEntityCount, returnedRatingEntityCount);
        verify(ratingRepository, times(1))
                .countByDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void shouldBeReturnRatingEntityCountByDateTimeBetweenGroupedByIsCritical() {
        Long ratingEntityCount = 20L;
        when(ratingRepository.countByDateTimeBetweenAndIsCritical(any(LocalDateTime.class), any(LocalDateTime.class), any(Boolean.class)))
                .thenReturn(ratingEntityCount);

        Map<String, Long> returnedRatingEntityCount = ratingService.countRatingByDateTimeBetweenGroupedByIsCritical(
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now()
        );

        assertEquals(ratingEntityCount, returnedRatingEntityCount.get("CRITICAL"));
        assertEquals(ratingEntityCount, returnedRatingEntityCount.get("NORMAL"));
        verify(ratingRepository, times(2))
                .countByDateTimeBetweenAndIsCritical(any(LocalDateTime.class), any(LocalDateTime.class), anyBoolean());
    }

}