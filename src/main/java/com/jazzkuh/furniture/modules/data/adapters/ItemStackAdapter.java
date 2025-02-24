package com.jazzkuh.furniture.modules.data.adapters;

import com.google.gson.*;
import com.jazzkuh.furniture.utils.serialization.ItemstackSerialization;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement == null) return new ItemStack(Material.AIR);
        if (jsonElement.getAsJsonObject().get("compoundTag") != null) return new ItemStack(Material.AIR);

        String data = jsonElement.getAsJsonObject().get("data").getAsString();
        return ItemstackSerialization.deserializeItemStack(data);
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        if (itemStack == null) return null;
        if (itemStack.getType() == Material.AIR) return null;

        String data = ItemstackSerialization.serialize(itemStack);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data", data);
        return jsonObject;
    }
}