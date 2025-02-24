package com.jazzkuh.furniture.modules.data.mongo;

import com.google.gson.Gson;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;

import java.util.HashMap;
import java.util.Map;

public final class MongoClient {
    private final com.mongodb.client.MongoClient mongoClient;
    private final Map<String, MongoScope> databaseMap;
    private final Gson gson;

    public MongoClient(String host, int port, String user, String password, Gson gson) {
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                .applyConnectionString(new ConnectionString("mongodb://" + user + ":" + password + "@" + host + ":" + port)).build();
        this.mongoClient = MongoClients.create(mongoClientSettings);
        this.gson = gson;
        this.databaseMap = new HashMap<>();
    }

    public MongoScope getScopeInternal(String database) {
        return databaseMap.computeIfAbsent(database, name -> new MongoScope(mongoClient.getDatabase(name), gson));
    }

    public void close() {
        mongoClient.close();
    }
}
