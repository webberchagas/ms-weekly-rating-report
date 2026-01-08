package br.com.fiap.report.rating.repository;

import br.com.fiap.report.rating.dto.RatingCountByDateDTO;
import br.com.fiap.report.rating.dto.RatingReportDataDTO;
import br.com.fiap.report.rating.entity.RatingEntity;
import br.com.fiap.report.rating.repository.mongo.MongoClientProvider;
import br.com.fiap.report.rating.util.EnvUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RatingRepository {

    private final MongoCollection<Document> collection;

    public RatingRepository() {
        String dbName = EnvUtil.getEnvOrElseThrow("MONGO_DB");
        String collectionName = EnvUtil.getEnvOrElseThrow("MONGO_COLLECTION");

        MongoDatabase db = MongoClientProvider.getMongoClient().getDatabase(dbName);
        this.collection = db.getCollection(collectionName);
    }

    public List<RatingEntity> findByDateTimeBetween(LocalDateTime start, LocalDateTime end) {
        Date startDate = Date.from(start.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate   = Date.from(end.atZone(ZoneId.systemDefault()).toInstant());

        Bson filter = Filters.and(
                Filters.gte("createdAt", startDate),
                Filters.lte("createdAt", endDate)
        );

        List<RatingEntity> results = new ArrayList<>();

        collection.find(filter).forEach(doc -> results.add(toEntity(doc)));

        return results;
    }

    public Long countByDateTimeBetween(LocalDateTime start, LocalDateTime end) {
        ZoneId zone = ZoneOffset.UTC;

        Date startDate = Date.from(start.atZone(zone).toInstant());
        Date endDate   = Date.from(end.atZone(zone).toInstant());

        Bson filter = Filters.and(
                Filters.gte("createdAt", startDate),
                Filters.lte("createdAt", endDate)
        );

        return collection.countDocuments(filter);
    }

    public Long countByDateTimeBetweenAndIsCritical(LocalDateTime start, LocalDateTime end, Boolean isCritical) {
        ZoneId zone = ZoneOffset.UTC;

        Date startDate = Date.from(start.atZone(zone).toInstant());
        Date endDate   = Date.from(end.atZone(zone).toInstant());

        Bson filter = Filters.and(
                Filters.gte("createdAt", startDate),
                Filters.lte("createdAt", endDate),
                Filters.eq("critical", isCritical)
        );

        return collection.countDocuments(filter);
    }

    public List<RatingCountByDateDTO> countRatingByDateTimeBetweenGroupedByDate(LocalDateTime start, LocalDateTime end) {
        ZoneId zone = ZoneOffset.UTC;

        Date startDate = Date.from(start.atZone(zone).toInstant());
        Date endDate   = Date.from(end.atZone(zone).toInstant());

        List<Bson> pipeline = List.of(

                // MATCH
                Aggregates.match(
                        Filters.and(
                                Filters.gte("createdAt", startDate),
                                Filters.lte("createdAt", endDate)
                        )
                ),

                // PROJECT (format date)
                Aggregates.project(
                        Projections.fields(
                                Projections.computed(
                                        "date",
                                        new Document("$dateToString",
                                                new Document("format", "%Y-%m-%d")
                                                        .append("date", "$createdAt")
                                        )
                                )
                        )
                ),

                // GROUP
                Aggregates.group(
                        "$date",
                        Accumulators.sum("total", 1)
                ),

                // PROJECT FINAL
                Aggregates.project(
                        Projections.fields(
                                Projections.computed("date", "$_id"),
                                Projections.include("total")
                        )
                ),

                // SORT
                Aggregates.sort(Sorts.ascending("date"))
        );

        List<RatingCountByDateDTO> result = new LinkedList<>();

        collection.aggregate(pipeline).forEach(doc -> {
                        Number total = doc.get("total", Number.class);

                        result.add(
                                new RatingCountByDateDTO(
                                        doc.getString("date"),
                                        total.longValue()
                                )
                        );
                }
        );

        return result;
    }

    private RatingEntity toEntity(Document doc) {
        return new RatingEntity(
                extractId(doc),
                doc.getString("description"),
                doc.getInteger("rating"),
                doc.getBoolean("critical"),
                doc.getDate("createdAt")
                        .toInstant()
                        .atZone(ZoneOffset.UTC)
                        .toLocalDateTime()
        );
    }

    private String extractId(Document doc) {
        Object id = doc.get("_id");
        return id instanceof ObjectId
                ? ((ObjectId) id).toHexString()
                : id.toString();
    }

}
