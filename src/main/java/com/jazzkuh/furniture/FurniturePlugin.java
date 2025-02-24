package com.jazzkuh.furniture;


import com.jazzkuh.furniture.modules.configuration.DefaultConfiguration;
import com.jazzkuh.commandlib.spigot.SpigotCommandLoader;
import com.jazzkuh.modulemanager.spigot.SpigotModuleManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.LoggerFactory;

public final class FurniturePlugin extends JavaPlugin {
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private static FurniturePlugin instance;

    @Getter
    private static SpigotModuleManager<FurniturePlugin> moduleManager;

    @Getter
    private static DefaultConfiguration defaultConfiguration;

    public FurniturePlugin() {
        setInstance(this);
        moduleManager = new SpigotModuleManager<>(this, LoggerFactory.getLogger("FurniturePlugin"));
    }

    @Override
    public void onLoad() {
        defaultConfiguration = new DefaultConfiguration();
        defaultConfiguration.saveConfiguration();

        moduleManager.scanModules(getClass());
        moduleManager.load();
    }

    @Override
    public void onEnable() {
        SpigotCommandLoader.loadResolvers();

        moduleManager.enable();

        this.getLogger().info("Running with " + moduleManager.getModules().size() + " modules enabled.");
    }

    @Override
    public void onDisable() {
        moduleManager.disable();
    }
}
