package br.com.fiap.report.rating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RatingReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatingReportApplication.class, args);
	}

}
