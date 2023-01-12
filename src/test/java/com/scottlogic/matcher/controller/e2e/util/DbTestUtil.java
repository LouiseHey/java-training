package com.scottlogic.matcher.controller.e2e.util;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.scottlogic.matcher.models.User;
import net.minidev.json.JSONObject;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.bson.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.mongodb.client.model.Filters.eq;
import static io.restassured.RestAssured.given;

public class DbTestUtil {

    public static void insertUser(User user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        try (MongoClient mongoClient = MongoClients.create(TestConstants.DATABASE_CONNECTION)) {
            MongoDatabase database = mongoClient.getDatabase(TestConstants.DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(TestConstants.DATABASE_USER_COLLECTION);
            collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("username", user.getUsername())
                    .append("password", passwordEncoder.encode(user.getPassword())));
        }
    }

    public static void deleteUser(String username) {
        try (MongoClient mongoClient = MongoClients.create(TestConstants.DATABASE_CONNECTION)) {
            MongoDatabase database = mongoClient.getDatabase(TestConstants.DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(TestConstants.DATABASE_USER_COLLECTION);

            Bson query = eq("username", username);
            collection.deleteOne(query);
        }
    }

    public static void cleanOrders() {
        try (MongoClient mongoClient = MongoClients.create(TestConstants.DATABASE_CONNECTION)) {
            MongoDatabase database = mongoClient.getDatabase(TestConstants.DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(TestConstants.DATABASE_ORDER_COLLECTION);

            BasicDBObject document = new BasicDBObject();
            collection.deleteMany(document);
        }
    }

    public static void cleanTrades() {
        try (MongoClient mongoClient = MongoClients.create(TestConstants.DATABASE_CONNECTION)) {
            MongoDatabase database = mongoClient.getDatabase(TestConstants.DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(TestConstants.DATABASE_TRADE_COLLECTION);

            BasicDBObject document = new BasicDBObject();
            collection.deleteMany(document);
        }
    }

    public static String retrieveToken(User user) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", user.getUsername());
        requestParams.put("password", user.getPassword());

        return TestConstants.AUTH_PREFIX + given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).
                when().
                post("/auth/signin").
                then().
                extract().
                header(TestConstants.AUTH_HEADER);
    }
}
