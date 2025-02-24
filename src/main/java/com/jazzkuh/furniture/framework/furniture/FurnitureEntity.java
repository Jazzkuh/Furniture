package com.jazzkuh.furniture.framework.furniture;

import com.jazzkuh.furniture.utils.LocationUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@Getter
public class FurnitureEntity {
    private final ItemDisplay itemDisplay;
    private final PersistentDataContainer persistentDataContainer;
    private final String identifier;
    private final Location blockLocation;
    private final FurnitureSize furnitureSize;
    private final Boolean transparent;

    @SneakyThrows
    @SuppressWarnings("ConstantConditions")
    public FurnitureEntity(ItemDisplay itemDisplay) {
        this.itemDisplay = itemDisplay;
        this.persistentDataContainer = itemDisplay.getPersistentDataContainer();

        this.identifier = persistentDataContainer.get(new NamespacedKey("furniture", "identifier"), PersistentDataType.STRING);
        this.blockLocation = LocationUtils.toLocation(persistentDataContainer.get(new NamespacedKey("furniture", "blocklocation"), PersistentDataType.STRING));
        this.furnitureSize = FurnitureSize.valueOf(persistentDataContainer.get(new NamespacedKey("furniture", "size"), PersistentDataType.STRING));

        if (persistentDataContainer.has(new NamespacedKey("furniture", "transparent"), PersistentDataType.STRING)) {
            this.transparent = Boolean.valueOf(persistentDataContainer.get(new NamespacedKey("furniture", "transparent"), PersistentDataType.STRING));
        } else {
            this.transparent = false;
        }
    }
}
