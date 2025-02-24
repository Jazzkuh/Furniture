package com.jazzkuh.furniture.modules.furniture.models;

import com.jazzkuh.furniture.modules.data.mongo.MongoEntity;
import com.jazzkuh.furniture.modules.data.mongo.MongoObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@ToString
@MongoEntity(collection = "furniture_models")
public final class FurnitureModel extends MongoObject<String> {
    private ItemStack itemStack;

    public FurnitureModel(String key) {
        super(key);
    }
}