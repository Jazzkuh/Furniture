package com.jazzkuh.furniture.modules.data.mongo;

import com.mongodb.client.MongoCollection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.TYPE})
public @interface MongoEntity {

    /**
     * Name of {@link MongoCollection} collection
     *
     * @return name of MongoCollection
     */
    String collection();

}
