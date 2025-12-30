package br.com.fiap.report.rating.schedule;

import br.com.fiap.report.rating.config.AppConfig;
import br.com.fiap.report.rating.dto.queue.RatingReportDTO;
import br.com.fiap.report.rating.dto.response.RatingCountByDateDTO;
import br.com.fiap.report.rating.service.RatingService;
import br.com.fiap.report.rating.service.impl.RatingServiceImpl;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.TimerTrigger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

public class WeeklyRatingReportSchedule {

    private static ApplicationContext context;

    static {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    @FunctionName("WeeklyRatingReport")
    public void run(
        @TimerTrigger(
                name = "WeeklyRatingReportTrigger",
                schedule = "59 23 * * 6 *"
        ) String timerInfo,
        final ExecutionContext executionContext
    ) {
        executionContext.getLogger().info("=== Emitindo relatório semanal de avaliações ===");

        try {
            RatingService ratingService = context.getBean(RatingService.class);

            LocalDateTime end = LocalDateTime.now();
            LocalDateTime start = end.minusDays(1);

            RatingReportDTO results = ratingService.getRatingReportByDateTimeBetween(start, end);

            executionContext.getLogger().info(results.toString());
        } catch (Exception e) {
            executionContext.getLogger().severe("Erro no relatório semanal: " + e.getMessage());
        }
    }

}
