package com.jazzkuh.furniture.modules.configuration;

import com.jazzkuh.furniture.FurniturePlugin;
import com.jazzkuh.furniture.utils.configuration.ConfigurateConfig;
import lombok.Getter;

@Getter
public class DefaultConfiguration extends ConfigurateConfig {
    private final String version;

    private final String mongoAddress;
    private final Integer mongoPort;
    private final String mongoUsername;
    private final String mongoPassword;

    public DefaultConfiguration() {
        super(FurniturePlugin.getInstance().getDataPath(), "config.yml");

        this.version = rootNode.node("_version").getString("1");

        this.mongoAddress = rootNode.node("mongodb", "address").getString("localhost");
        this.mongoPort = rootNode.node("mongodb", "port").getInt(27017);
        this.mongoUsername = rootNode.node("mongodb", "username").getString("CHANGE_ME");
        this.mongoPassword = rootNode.node("mongodb", "password").getString("CHANGE_ME");
    }
}
