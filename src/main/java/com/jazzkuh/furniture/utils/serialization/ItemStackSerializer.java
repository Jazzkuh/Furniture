package com.jazzkuh.furniture.utils.serialization;

import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ItemStackSerializer implements TypeSerializer<ItemStack> {
    private ConfigurationNode nonVirtualNode(final ConfigurationNode source, final Object... path) throws SerializationException {
        if (!source.hasChild(path)) {
            throw new SerializationException("Required field " + Arrays.toString(path) + " was not present in node");
        }
        return source.node(path);
    }
    
    @Override
    @SneakyThrows
    public ItemStack deserialize(final Type type, final ConfigurationNode source) {
        final ConfigurationNode configurationNode = nonVirtualNode(source, "base64");
        final String base64 = configurationNode.getString();
        if (base64 == null) {
            throw new SerializationException("Required field 'base64' was not present in node");
        }

        return ItemstackSerialization.deserializeItemStack(base64);
    }

    @Override
    public void serialize(final Type type, final @Nullable ItemStack itemStack, final ConfigurationNode target) throws SerializationException {
        if (itemStack == null) {
            target.raw(null);
            return;
        }

        target.node("base64").set(ItemstackSerialization.serialize(itemStack));
    }
}