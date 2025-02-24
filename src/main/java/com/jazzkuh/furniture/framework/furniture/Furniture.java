package com.jazzkuh.furniture.framework.furniture;

import com.jazzkuh.furniture.utils.LocationUtils;
import com.jazzkuh.furniture.utils.PersistentData;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Furniture {
    @Getter private final ItemStack itemStack;
    @Getter private final String identifier;
    @Getter private final FurnitureSize furnitureSize;
    @Getter private final Boolean transparent;

    @SneakyThrows
    public Furniture(ItemStack itemStack) {
        this.itemStack = itemStack;

        this.identifier = PersistentData.getString(itemStack, "furniture_identifier");
        this.furnitureSize = FurnitureSize.valueOf(PersistentData.getString(itemStack, "furniture_size"));
        this.transparent = PersistentData.getBoolean(itemStack, "furniture_transparent");
    }

    public void placeBlock(Location location) {
        Location blockLocation = location.clone().getBlock().getRelative(BlockFace.UP).getLocation();
        if (blockLocation.getBlock().getType() != Material.AIR) return;

        ItemDisplay itemDisplay = location.getWorld().spawn(location.clone().add(0, 2.35D, 0), ItemDisplay.class);
        itemDisplay.setInvulnerable(true);
        itemDisplay.setGravity(false);
        itemDisplay.setItemStack(this.getItemStack());
        itemDisplay.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
        itemDisplay.setViewRange(0.595F);
        itemDisplay.setRotation(location.getYaw(), location.getPitch());
        itemDisplay.setTransformationMatrix(new Matrix4f().scale(0.5F));

        if (!transparent) {
            blockLocation.getBlock().setType(Material.BARRIER);
            if (furnitureSize == FurnitureSize.LARGE)
                blockLocation.getBlock().getRelative(BlockFace.UP).setType(Material.BARRIER);
        }

        PersistentDataContainer persistentDataContainer = itemDisplay.getPersistentDataContainer();
        persistentDataContainer.set(new NamespacedKey("furniture", "identifier"), PersistentDataType.STRING, this.getIdentifier());
        persistentDataContainer.set(new NamespacedKey("furniture", "blocklocation"), PersistentDataType.STRING, LocationUtils.toString(blockLocation));
        persistentDataContainer.set(new NamespacedKey("furniture", "size"), PersistentDataType.STRING, this.getFurnitureSize().name());
        persistentDataContainer.set(new NamespacedKey("furniture", "transparent"), PersistentDataType.STRING, transparent.toString());
    }
}
