package com.jazzkuh.furniture.modules.furniture.listeners;

import com.jazzkuh.furniture.FurniturePlugin;
import com.jazzkuh.furniture.framework.furniture.FurnitureEntity;
import com.jazzkuh.furniture.framework.furniture.FurnitureSize;
import com.jazzkuh.furniture.modules.furniture.FurnitureModule;
import com.jazzkuh.furniture.utils.ChatUtils;
import com.jazzkuh.furniture.utils.ItemBuilder;
import com.jazzkuh.furniture.utils.PersistentData;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class FurnitureRemoveEntityListener implements Listener {
    private final FurnitureModule furnitureModule = FurniturePlugin.getModuleManager().get(FurnitureModule.class);

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (PersistentData.contains(itemStack, "furniture_identifier")) return;

        if (player.getGameMode() != GameMode.CREATIVE) return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        Block block = clickedBlock.getLocation().add(0, 1, 0).getBlock();

        for (Entity entity : block.getWorld().getNearbyEntities(block.getLocation(), 1, 1.5, 1)) {
            if (!(entity instanceof ItemDisplay display)) continue;

            FurnitureEntity furnitureEntity = furnitureModule.getFurnitureEntity(display);
            if (furnitureEntity == null) continue;
            if (!furnitureEntity.getTransparent()) continue;

            Location foundLocation = furnitureEntity.getBlockLocation();
            if (furnitureEntity.getBlockLocation() == null) continue;

            if (block.getLocation().getBlockX() != foundLocation.getBlockX() || block.getLocation().getBlockZ() != foundLocation.getBlockZ()) continue;
            if (!player.hasPermission("furniture.common.break")) {
                player.sendMessage(ChatUtils.prefix("Furniture", "<error>You do not have permission to remove this."));
                event.setCancelled(true);
                return;
            }

            if (!player.isSneaking()) return;
            if (foundLocation.getBlock().getType() == Material.BARRIER) {
                foundLocation.getBlock().setType(Material.AIR);
                if (furnitureEntity.getFurnitureSize() == FurnitureSize.LARGE && foundLocation.add(0, 1, 0).getBlock().getType() == Material.BARRIER) {
                    foundLocation.add(0, 1, 0).getBlock().setType(Material.AIR);
                }
            }

            player.sendMessage(ChatUtils.prefix("Furniture", "<gray>Removed furniture."));
            display.remove();
            return;
        }
    }
}
