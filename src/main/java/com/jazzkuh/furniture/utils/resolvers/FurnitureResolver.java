package com.jazzkuh.furniture.utils.resolvers;

import com.jazzkuh.commandlib.common.AnnotationCommandSender;
import com.jazzkuh.commandlib.common.resolvers.CompletionResolver;
import com.jazzkuh.commandlib.common.resolvers.ContextResolver;
import com.jazzkuh.furniture.FurniturePlugin;
import com.jazzkuh.furniture.modules.furniture.FurnitureModule;
import com.jazzkuh.furniture.modules.furniture.models.FurnitureModel;
import org.bukkit.command.CommandSender;

import java.util.List;

public final class FurnitureResolver implements ContextResolver<FurnitureModel>, CompletionResolver<CommandSender> {
    private final FurnitureModule furnitureModule = FurniturePlugin.getModuleManager().get(FurnitureModule.class);

    @Override
    public List<String> resolve(AnnotationCommandSender<CommandSender> annotationCommandSender, String string) {
        return furnitureModule.getCustomBlocks().keySet().stream().toList();
    }

    @Override
    public FurnitureModel resolve(String args) {
        return furnitureModule.getFurnitureModel(args);
    }
}