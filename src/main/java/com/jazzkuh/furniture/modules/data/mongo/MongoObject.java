package com.jazzkuh.furniture.modules.data.mongo;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public abstract class MongoObject<T> {

    /**
     * key to identify object
     */
    @Getter
    @SerializedName("_id")
    private final T key;

    protected MongoObject(T key) {
        this.key = key;
    }
}
