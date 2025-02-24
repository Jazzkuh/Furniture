package com.jazzkuh.furniture.utils.configuration;

import lombok.Getter;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.util.MapFactories;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

@Getter
public abstract class ConfigurateConfig {
    protected final YamlConfigurationLoader loader;
    protected CommentedConfigurationNode rootNode;

    public ConfigurateConfig(Path path, String name) {
        loader = YamlConfigurationLoader.builder()
                .path(path.resolve(name))
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .headerMode(HeaderMode.PRESET)
                .defaultOptions(options -> {
                    options = options.mapFactory(MapFactories.sortedNatural());
                    return options;
                })
                .build();

        try {
            rootNode = loader.load();
        } catch (IOException e) {
        }
    }

    public void saveConfiguration() {
        try {
            loader.save(rootNode);
        } catch (final ConfigurateException e) {
        }
    }
}
