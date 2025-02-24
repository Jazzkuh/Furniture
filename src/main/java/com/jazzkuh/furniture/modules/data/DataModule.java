package com.jazzkuh.furniture.modules.data;

import com.google.gson.GsonBuilder;
import com.jazzkuh.furniture.FurniturePlugin;
import com.jazzkuh.furniture.modules.data.adapters.ItemStackAdapter;
import com.jazzkuh.furniture.modules.data.mongo.MongoClient;
import com.jazzkuh.furniture.modules.data.mongo.MongoScope;
import com.jazzkuh.modulemanager.spigot.SpigotModule;
import com.jazzkuh.modulemanager.spigot.SpigotModuleManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

public class DataModule extends SpigotModule<FurniturePlugin> {
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private MongoClient mongoClient;
		
	@Getter 
	@Setter(AccessLevel.PRIVATE)
	private MongoScope mongoScope;

	public DataModule(SpigotModuleManager<FurniturePlugin> moduleManager) {
		super(moduleManager);
	}

	@Override
	public void onEnable() {
		String host = FurniturePlugin.getDefaultConfiguration().getMongoAddress();
		int port = FurniturePlugin.getDefaultConfiguration().getMongoPort();
		String user = FurniturePlugin.getDefaultConfiguration().getMongoUsername();
		String password = FurniturePlugin.getDefaultConfiguration().getMongoPassword();

		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter());

		setMongoClient(new MongoClient(host, port, user, password, builder.create()));
		setMongoScope(this.mongoClient.getScopeInternal("furniture_data"));
	}

	@Override
	public void onDisable() {
		this.mongoClient.close();
	}
}