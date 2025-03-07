package com.jazzkuh.furniture.modules.furniture;

import com.jazzkuh.commandlib.common.resolvers.Resolvers;
import com.jazzkuh.furniture.FurniturePlugin;
import com.jazzkuh.furniture.framework.furniture.Furniture;
import com.jazzkuh.furniture.framework.furniture.FurnitureEntity;
import com.jazzkuh.furniture.modules.data.DataModule;
import com.jazzkuh.furniture.modules.furniture.commands.FurnitureCommand;
import com.jazzkuh.furniture.modules.furniture.commands.ItemModifierCommand;
import com.jazzkuh.furniture.modules.furniture.listeners.*;
import com.jazzkuh.furniture.modules.furniture.models.FurnitureModel;
import com.jazzkuh.furniture.utils.PersistentData;
import com.jazzkuh.furniture.utils.resolvers.FurnitureResolver;
import com.jazzkuh.modulemanager.spigot.SpigotModule;
import com.jazzkuh.modulemanager.spigot.SpigotModuleManager;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FurnitureModule extends SpigotModule<FurniturePlugin> {
    private DataModule dataModule;
    private final Map<String, FurnitureModel> furnitureModels = new HashMap<>();

    public FurnitureModule(SpigotModuleManager<FurniturePlugin> moduleManager, DataModule dataModule) {
        super(moduleManager);
    }

    @Override
    public void onLoad() {
        dataModule = getOwningManager().get(DataModule.class);
    }

    @Override
    public void onEnable() {
        Resolvers.register(FurnitureModel.class, new FurnitureResolver());

        loadFurniture();

        registerComponent(new FurnitureCommand(this, dataModule));
        registerComponent(new ItemModifierCommand());

        registerComponent(new FurniturePlaceListener(this));
        registerComponent(new FurnitureBreakListener(this));
        registerComponent(new FurnitureRemoveEntityListener());
    }

    public void loadFurniture() {
        this.furnitureModels.clear();
        dataModule.getMongoScope().readAllAsync(FurnitureModel.class).whenComplete((customBlockModels, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }

            for (FurnitureModel furnitureModel : customBlockModels) {
                furnitureModels.put(furnitureModel.getKey(), furnitureModel);
            }
            this.getLogger().info("Loaded " + furnitureModels.size() + " custom blocks.");
        });
    }

    @Nullable
    public FurnitureModel getFurnitureModel(String identifier) {
        return furnitureModels.get(identifier);
    }

    @Nullable
    public Furniture getFurniture(ItemStack itemStack) {
        if (!PersistentData.contains(itemStack, "furniture_identifier")) return null;
        return new Furniture(itemStack);
    }

    public FurnitureEntity getFurnitureEntity(ItemDisplay display) {
        PersistentDataContainer persistentDataContainer = display.getPersistentDataContainer();
        if (!persistentDataContainer.has(new NamespacedKey("furniture", "identifier"))) return null;
        return new FurnitureEntity(display);
    }
}
