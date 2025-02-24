package com.jazzkuh.furniture.modules.furniture.listeners;

import com.jazzkuh.furniture.framework.furniture.FurnitureEntity;
import com.jazzkuh.furniture.framework.furniture.FurnitureSize;
import com.jazzkuh.furniture.modules.furniture.FurnitureModule;
import com.jazzkuh.furniture.utils.ChatUtils;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@AllArgsConstructor
public class FurnitureBreakListener implements Listener {
    private FurnitureModule furnitureModule;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getType() != Material.BARRIER) return;
        for (Entity entity : block.getWorld().getNearbyEntities(block.getLocation(), 1, 1.5, 1)) {
            if (!(entity instanceof ItemDisplay display)) continue;

            FurnitureEntity furnitureEntity = furnitureModule.getFurnitureEntity(display);
            if (furnitureEntity == null) continue;

            Location foundLocation = furnitureEntity.getBlockLocation();
            if (furnitureEntity.getBlockLocation() == null) continue;

            if (block.getLocation().getBlockX() != foundLocation.getBlockX() || block.getLocation().getBlockZ() != foundLocation.getBlockZ()) continue;
            if (!player.hasPermission("furniture.common.break")) {
                player.sendMessage(ChatUtils.prefix("Furniture", "<error>You do not have permission to break this."));
                event.setCancelled(true);
                return;
            }

            foundLocation.getBlock().setType(Material.AIR);
            if (furnitureEntity.getFurnitureSize() == FurnitureSize.LARGE) {
                foundLocation.add(0, 1, 0).getBlock().setType(Material.AIR);
            }

            display.remove();
            return;
        }
    }
}
