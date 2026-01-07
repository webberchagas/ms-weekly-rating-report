package br.com.fiap.report.rating.repository.mongo;

import br.com.fiap.report.rating.util.EnvUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoClientProvider {

    private static MongoClient mongoClient;

    private MongoClientProvider() {}

    public static MongoClient getMongoClient() {
        if (mongoClient == null) {
            String uri = EnvUtil.getEnvOrElseThrow("MONGODB_URI");

            mongoClient = MongoClients.create(uri);
        }

        return mongoClient;
    }

}
