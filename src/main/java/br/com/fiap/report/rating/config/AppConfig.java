package br.com.fiap.report.rating.config;

import br.com.fiap.report.rating.repository.RatingRepository;
import br.com.fiap.report.rating.service.RatingService;
import br.com.fiap.report.rating.service.impl.RatingServiceImpl;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "br.com.fiap.report.rating.repository")
public class AppConfig {

    @Bean
    public MongoClient mongoClient() {
        String connectionString = System.getenv("MONGO_URL");
        return MongoClients.create(connectionString);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, System.getenv("MONGO_DATABASE"));
    }

    @Bean
    public RatingService ratingService(
            MongoTemplate mongoTemplate,
            RatingRepository ratingRepository) {
        return new RatingServiceImpl(ratingRepository, mongoTemplate);
    }

}
