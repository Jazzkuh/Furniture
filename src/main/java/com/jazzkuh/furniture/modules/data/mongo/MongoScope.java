package com.jazzkuh.furniture.modules.data.mongo;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import lombok.Setter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public final class MongoScope {
    private static final JsonWriterSettings WRITER_SETTINGS = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();
    private static final Executor EXECUTOR = Executors.newCachedThreadPool();
    private final MongoDatabase mongoDatabase;
    @Setter
    private Gson gson;
    
    public MongoScope(MongoDatabase mongoDatabase, Gson gson) {
        this.mongoDatabase = mongoDatabase;
        this.gson = gson;
    }

    /**
     * Gets {@link MongoEntity} annotation from value field in clazz
     *
     * @param clazz to get name of collection from
     * @return MongoCollection
     */
    private MongoCollection<Document> getCollection(Class<?> clazz) {
        MongoEntity entity = clazz.getAnnotation(MongoEntity.class);
        if (entity == null)
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not annotated with @MongoEntity");
        return mongoDatabase.getCollection(entity.collection());
    }

    /**
     * Update value in object.
     * Executes sync
     *
     * @param object object to update value in
     * @param field  field to update
     * @param value  value to update
     * @param <O>    instance of object updated
     */
    public <O extends MongoObject<?>> UpdateResult update(O object, String field, Object value) {
        return getCollection(object.getClass()).updateOne(eq("_id", object.getKey().toString()), set(field, value));
    }

    /**
     * Update value in object
     * Executes sync
     *
     * @param clazz  Collection to update value in
     * @param filter filter for custom query
     * @param field  field to update
     * @param value  value to update
     * @param <O>    instance of object updated
     */
    public <O extends MongoObject<?>> UpdateResult update(Class<O> clazz, Bson filter, String field, Object value) {
        return getCollection(clazz).updateOne(filter, set(field, value));
    }

    /**
     * Update value in object
     * Executes sync
     *
     * @param clazz  Collection to update value in
     * @param filter filter for custom query
     * @param update Bson update
     * @param <O>    instance of object updated
     */
    public <O extends MongoObject<?>> UpdateResult update(Class<O> clazz, Bson filter, Bson update) {
        return getCollection(clazz).updateOne(filter, update);
    }

    /**
     * Update value in object
     * Executes async
     *
     * @param object object to update value in
     * @param field  field to update
     * @param value  value to update
     * @param <O>    instance of object updated
     * @return CompletableFuture
     */
    public <O extends MongoObject<?>> CompletableFuture<UpdateResult> updateAsync(O object, String field, Object value) {
        return CompletableFuture.supplyAsync(() -> update(object, field, value), EXECUTOR);
    }

    /**
     * Update value in object
     * Executes async
     *
     * @param clazz  Collection to update value in
     * @param filter filter for custom query
     * @param field  field to update
     * @param value  value to update
     * @param <O>    instance of object updated
     * @return CompletableFuture
     */
    public <O extends MongoObject<?>> CompletableFuture<UpdateResult> updateAsync(Class<O> clazz, Bson filter, String field, Object value) {
        return CompletableFuture.supplyAsync(() -> update(clazz, filter, field, value), EXECUTOR);
    }

    /**
     * Update value in object
     * Executes async
     *
     * @param clazz  Collection to update value in
     * @param filter filter for custom query
     * @param update Bson update
     * @param <O>    instance of object updated
     */
    public <O extends MongoObject<?>> CompletableFuture<UpdateResult> updateAsync(Class<O> clazz, Bson filter, Bson update) {
        return CompletableFuture.supplyAsync(() -> update(clazz, filter, update), EXECUTOR);
    }

    /**
     * Update object in mongo
     * Executes sync
     *
     * @param object to update
     * @param <O>    instance of object saved
     */
    public <O extends MongoObject<?>> void save(O object) {
        Document document = Document.parse(gson.toJson(object));
        getCollection(object.getClass()).replaceOne(eq("_id", document.get("_id")), document, new ReplaceOptions().upsert(true));
    }

    /**
     * Update object in mongo
     * Executes async
     *
     * @param object to update
     * @param <O>    instance of object saved
     * @return CompletableFuture
     */
    public <O extends MongoObject<?>> CompletableFuture<Void> saveAsync(O object) {
        return CompletableFuture.runAsync(() -> save(object), EXECUTOR);
    }

    /**
     * Delete object in mongo
     * Executes sync
     *
     * @param object to delete
     * @param <O>    instance of object deleted
     */
    public <O extends MongoObject<?>> void delete(O object) {
        Document document = new Document().append("_id", object.getKey().toString());
        getCollection(object.getClass()).deleteOne(document);
    }

    /**
     * Delete object in mongo
     * Executes async
     *
     * @param object to delete
     * @param <O>    instance of object deleted
     * @return CompletableFuture
     */
    public <O extends MongoObject<?>> CompletableFuture<Void> deleteAsync(O object) {
        return CompletableFuture.runAsync(() -> delete(object), EXECUTOR);
    }

    /**
     * Pushes value in array
     * Executes sync
     *
     * @param object where array is in
     * @param field  with array
     * @param value  to push in array
     * @param <O>    MongoObject
     */
    public <O extends MongoObject<?>> void push(O object, String field, Object value) {
        getCollection(object.getClass()).updateOne(eq("_id", object.getKey().toString()), Updates.push(field, Document.parse(gson.toJson(value))));
    }

    /**
     * Pushes value in array
     * Executes sync
     *
     * @param clazz  with collection
     * @param filter filter
     * @param field  with array
     * @param value  to push in array
     * @param <O>    MongoObject
     */
    public <O extends MongoObject<?>> void push(Class<O> clazz, Bson filter, String field, Object value) {
        getCollection(clazz).updateOne(filter, Updates.push(field, Document.parse(gson.toJson(value))));
    }

    /**
     * Pushes value in array
     * Executes sync
     *
     * @param object where array is in
     * @param field  with array
     * @param values to push in array
     * @param <O>    MongoObject
     */
    public <O extends MongoObject<?>> void push(O object, String field, List<?> values) {
        MongoCollection<Document> mongoCollection = getCollection(object.getClass());
        Bson update = Updates.combine(values.stream().map(value -> Updates.push(field, Document.parse(gson.toJson(value)))).collect(Collectors.toList()));
        mongoCollection.updateOne(eq("_id", object.getKey().toString()), update);
    }

    /**
     * Pushes values in array
     * Executes sync
     *
     * @param clazz  with collection
     * @param filter filter
     * @param field  with array
     * @param values to push in array
     * @param <O>    MongoObject
     */
    public <O extends MongoObject<?>> void push(Class<O> clazz, Bson filter, String field, List<?> values) {
        MongoCollection<Document> mongoCollection = getCollection(clazz);
        Bson update = Updates.combine(values.stream().map(value -> Updates.push(field, Document.parse(gson.toJson(value)))).collect(Collectors.toList()));
        mongoCollection.updateOne(filter, update);
    }

    /**
     * Pushes value in array
     * Executes async
     *
     * @param object where array is in
     * @param field  with array
     * @param value  to push in array
     * @param <O>    MongoObject
     * @return CompletableFuture
     */
    public <O extends MongoObject<?>> CompletableFuture<Void> pushAsync(O object, String field, Object value) {
        return CompletableFuture.runAsync(() -> push(object, field, value), EXECUTOR);
    }

    /**
     * Pushes value in array
     * Executes async
     *
     * @param clazz  with collection
     * @param filter filter
     * @param field  with array
     * @param value  to push in array
     * @param <O>    MongoObject
     * @return CompletableFuture
     */
    public <O extends MongoObject<?>> CompletableFuture<Void> pushAsync(Class<O> clazz, Bson filter, String field, Object value) {
        return CompletableFuture.runAsync(() -> push(clazz, filter, field, value), EXECUTOR);
    }

    /**
     * Pushes values in array
     * Executes async
     *
     * @param object where array is in
     * @param field  with array
     * @param values to push in array
     * @param <O>    MongoObject
     * @return CompletableFuture
     */
    public <O extends MongoObject<?>> CompletableFuture<Void> pushAsync(O object, String field, List<?> values) {
        return CompletableFuture.runAsync(() -> push(object, field, values), EXECUTOR);
    }

    /**
     * Pushes values in array
     * Executes async
     *
     * @param clazz  with collection
     * @param filter filter
     * @param field  with array
     * @param values to push in array
     * @param <O>    MongoObject
     * @return CompletableFuture
     */
    public <O extends MongoObject<?>> CompletableFuture<Void> pushAsync(Class<O> clazz, Bson filter, String field, List<?> values) {
        return CompletableFuture.runAsync(() -> push(clazz, filter, field, values), EXECUTOR);
    }

    /**
     * Pulls value in array
     * Executes sync
     *
     * @param object where array is in
     * @param field  with array
     * @param value  to push in array
     * @param <O>    MongoObject
     */
    public <O extends MongoObject<?>> void pull(O object, String field, Object value) {
        getCollection(object.getClass()).updateOne(eq("_id", object.getKey().toString()), Updates.pull(field, Document.parse(gson.toJson(value))));
    }

    /**
     * Pulls value in array
     * Executes sync
     *
     * @param clazz  with collection
     * @param filter filter
     * @param field  with array
     * @param value  to push in array
     * @param <O>    MongoObject
     */
    public <O extends MongoObject<?>> void pull(Class<O> clazz, Bson filter, String field, Object value) {
        getCollection(clazz).updateOne(filter, Updates.push(field, Document.parse(gson.toJson(value))));
    }

    /**
     * Pulls value in array
     * Executes async
     *
     * @param object where array is in
     * @param field  with array
     * @param value  to push in array
     * @param <O>    MongoObject
     * @return Instance of MongoObject
     */
    public <O extends MongoObject<?>> CompletableFuture<Void> pullAsync(O object, String field, Object value) {
        return CompletableFuture.runAsync(() -> push(object, field, value), EXECUTOR);
    }

    /**
     * Pulls value in array
     * Executes async
     *
     * @param clazz  with collection
     * @param filter filter
     * @param field  with array
     * @param value  to push in array
     * @param <O>    MongoObject
     * @return Instance of MongoObject
     */
    public <O extends MongoObject<?>> CompletableFuture<Void> pullAsync(Class<O> clazz, Bson filter, String field, Object value) {
        return CompletableFuture.runAsync(() -> push(clazz, filter, field, value), EXECUTOR);
    }

    /**
     * Reads mongo object from database and turns it into class
     * Executes sync
     *
     * @param clazz output class
     * @param key   Key to look for
     * @param <O>   Type of class
     * @param <K>   Type of key
     * @return Instance of MongoObject
     */
    public <O extends MongoObject<?>, K> O read(Class<O> clazz, K key) {
        MongoCollection<Document> mongoCollection = getCollection(clazz);

        Document document = mongoCollection.find(eq("_id", key)).first();

        if (document == null)
            return null;

        return gson.fromJson(document.toJson(WRITER_SETTINGS), clazz);
    }

    /**
     * Reads mongo object from database and turns it into class
     * Executes sync
     *
     * @param clazz  output class
     * @param filter to use as predicate
     * @param <O>    Type of class
     * @return Instance of MongoObject
     */
    public <O extends MongoObject<?>> O read(Class<O> clazz, Bson filter) {
        MongoCollection<Document> mongoCollection = getCollection(clazz);

        Document document = mongoCollection.find(filter).first();

        if (document == null) {
            return null;
        }

        return gson.fromJson(document.toJson(WRITER_SETTINGS), clazz);
    }


    /**
     * Reads mongo object from database and turns it into class
     * Executes async
     *
     * @param clazz output class
     * @param key   Key to look for
     * @param <O>   Type of class
     * @param <K>   Type of key
     * @return Instance of MongoObject
     */
    public <O extends MongoObject<?>, K> CompletableFuture<O> readAsync(Class<O> clazz, K key) {
        return CompletableFuture.supplyAsync(() -> read(clazz, key), EXECUTOR);
    }

    /**
     * Reads mongo object from database and turns it into class
     * Executes async
     *
     * @param clazz  output class
     * @param filter to use as predicate
     * @param <O>    Type of class
     * @return Instance of MongoObject
     */
    public <O extends MongoObject<?>> CompletableFuture<O> readAsync(Class<O> clazz, Bson filter) {
        return CompletableFuture.supplyAsync(() -> read(clazz, filter), EXECUTOR);
    }


    /**
     * Reads multiple mongo objects from database and turns it into class
     * Executes sync
     *
     * @param clazz  output class
     * @param filter to use as predicate
     * @param <O>    Type of class
     * @return Instance of MongoObject
     */
    public <O extends MongoObject<?>> List<O> readMany(Class<O> clazz, Bson filter) {
        MongoCollection<Document> mongoCollection = getCollection(clazz);

        return StreamSupport.stream(mongoCollection.find(filter).spliterator(), false)
                .map(document -> gson.fromJson(document.toJson(WRITER_SETTINGS), clazz))
                .collect(Collectors.toList());
    }

    /**
     * Reads multiple mongo objects from database and turns it into class
     * Executes async
     *
     * @param clazz  output class
     * @param filter to use as predicate
     * @param <O>    Type of class
     * @return Instance of MongoObject
     */
    public <O extends MongoObject<?>> CompletableFuture<List<O>> readManyAsync(Class<O> clazz, Bson filter) {
        return CompletableFuture.supplyAsync(() -> readMany(clazz, filter), EXECUTOR);
    }

    /**
     * Read all documents from collection
     * Executes sync
     *
     * @param clazz for output class
     * @param <O>   output class
     * @return List of mongo Objects
     */
    public <O extends MongoObject<?>> List<O> readAll(Class<O> clazz) {
        MongoCollection<Document> mongoCollection = getCollection(clazz);
        return StreamSupport.stream(mongoCollection.find().spliterator(), false)
                .map(document -> gson.fromJson(document.toJson(WRITER_SETTINGS), clazz))
                .toList();
    }

    /**
     * Read all documents from collection
     * Executes sync
     *
     * @param clazz  for output class
     * @param filter bson filter
     * @param <O>    output class
     * @return List of mongo Objects
     */
    public <O extends MongoObject<?>> List<O> readAll(Class<O> clazz, Bson filter) {
        MongoCollection<Document> mongoCollection = getCollection(clazz);
        return StreamSupport.stream(mongoCollection.find(filter).spliterator(), false)
                .map(document -> gson.fromJson(document.toJson(WRITER_SETTINGS), clazz))
                .toList();
    }

    /**
     * Read all documents from collection
     * Executes async
     *
     * @param clazz  for output class
     * @param filter bson filter
     * @param <O>    output class
     * @return List of mongo Objects
     */
    public <O extends MongoObject<?>> CompletableFuture<List<O>> readAllAsync(Class<O> clazz, Bson filter) {
        return CompletableFuture.supplyAsync(() -> readAll(clazz, filter), EXECUTOR);
    }

    /**
     * Read all documents from collection
     * Executes async
     *
     * @param clazz for output class
     * @param <O>   output class
     * @return List of mongo Objects
     */
    public <O extends MongoObject<?>> CompletableFuture<List<O>> readAllAsync(Class<O> clazz) {
        return CompletableFuture.supplyAsync(() -> readAll(clazz), EXECUTOR);
    }

    /**
     * Find and update class
     *
     * @param clazz  for output class
     * @param filter filter for custom query
     * @param update Bson to update
     * @param <O>    Type of clazz
     * @return Instance of object
     */
    public <O extends MongoObject<?>> O findOneAndUpdate(Class<O> clazz, Bson filter, Bson update) {
        MongoCollection<Document> mongoCollection = getCollection(clazz);
        Document document = mongoCollection.findOneAndUpdate(filter, update);

        if (document == null) {
            return null;
        }

        return gson.fromJson(document.toJson(WRITER_SETTINGS), clazz);
    }

    /**
     * Find and update class Async
     *
     * @param clazz  for output class
     * @param filter filter for custom query
     * @param update Bson to update
     * @param <O>    Type of clazz
     * @return Instance of object
     */
    public <O extends MongoObject<?>> CompletableFuture<O> findOneAndUpdateAsync(Class<O> clazz, Bson filter, Bson update) {
        return CompletableFuture.supplyAsync(() -> findOneAndUpdate(clazz, filter, update));
    }
}