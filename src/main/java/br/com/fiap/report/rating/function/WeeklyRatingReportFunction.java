package br.com.fiap.report.rating.function;

import br.com.fiap.report.rating.dto.RatingReportDTO;
import br.com.fiap.report.rating.service.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.ServiceBusQueueOutput;
import com.microsoft.azure.functions.annotation.TimerTrigger;

import java.time.LocalDateTime;

public class WeeklyRatingReportFunction {

    private final RatingService ratingService = new RatingService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FunctionName("WeeklyRatingReport")
    public void run(
        @TimerTrigger(
                name = "WeeklyRatingReportTrigger",
                schedule = "59 59 23 * * 7"
        ) String timerInfo,
        final ExecutionContext executionContext,
        @ServiceBusQueueOutput(
                name = "ratingReportMessage",
                queueName = "%QUEUE_WEEKLY_REPORT%",
                connection = "SERVICE_BUS_CONNECTION"
        ) OutputBinding<String> ratingReportMessage
    ) {
        executionContext.getLogger().info("=== Emitindo relatório semanal de avaliações ===");

        try {
            RatingReportDTO ratingReportDTO = ratingService.generateRatingReportDTO(
                    LocalDateTime.now().minusDays(7),
                    LocalDateTime.now()
            );

            ratingReportMessage.setValue(objectMapper.writeValueAsString(ratingReportDTO));

            executionContext.getLogger().info(objectMapper.writeValueAsString(ratingReportDTO));

            executionContext.getLogger().info("Relatório semanal de avaliações enviado para processamento.");
        } catch (Exception e) {
            executionContext.getLogger().severe("Erro no relatório semanal: " + e.getMessage());
        }

        executionContext.getLogger().info("=== Emissão do relatório semanal de avaliações finalizado ===");
    }

}
