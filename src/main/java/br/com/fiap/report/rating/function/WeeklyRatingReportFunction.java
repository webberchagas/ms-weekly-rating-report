package br.com.fiap.report.rating.function;

import br.com.fiap.report.rating.entity.RatingEntity;
import br.com.fiap.report.rating.repository.RatingRepository;
import br.com.fiap.report.rating.service.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.TimerTrigger;

import java.time.LocalDateTime;
import java.util.List;

public class WeeklyRatingReportFunction {

    private final RatingService ratingService = new RatingService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FunctionName("WeeklyRatingReport")
    public void run(
        @TimerTrigger(
            name = "WeeklyRatingReportTrigger",
            schedule = "0 */1 * * * *"
        ) String timerInfo,
        final ExecutionContext executionContext
    ) {
        executionContext.getLogger().info("=== Emitindo relatório semanal de avaliações ===");

        try {
            var result = ratingService.generateRatingReportDTO(
                    LocalDateTime.now().minusDays(7),
                    LocalDateTime.now()
            );

            executionContext.getLogger().info(objectMapper.writeValueAsString(result));
            executionContext.getLogger().info("Weekly report sent to Service Bus.");
        } catch (Exception e) {
            executionContext.getLogger().severe("Erro no relatório semanal: " + e.getMessage());
        }
    }

}
