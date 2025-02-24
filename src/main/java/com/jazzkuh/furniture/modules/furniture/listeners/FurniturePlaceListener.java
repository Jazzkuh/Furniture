package com.jazzkuh.furniture.modules.furniture.listeners;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jazzkuh.furniture.framework.furniture.Furniture;
import com.jazzkuh.furniture.framework.furniture.FurnitureSize;
import com.jazzkuh.furniture.modules.furniture.FurnitureModule;
import com.jazzkuh.furniture.utils.PersistentData;
import lombok.AllArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class FurniturePlaceListener implements Listener {
    private FurnitureModule furnitureModule;
    
    private final Cache<UUID, Long> cooldownCache = CacheBuilder.newBuilder().expireAfterWrite(600, TimeUnit.MILLISECONDS).build();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (block == null) return;

        if (cooldownCache.asMap().containsKey(player.getUniqueId())) return;
        if (itemStack.getType() == Material.AIR) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (player.getGameMode() == GameMode.ADVENTURE) return;

        if (block.getType() == Material.BARRIER) return;
        if (!block.isSolid()) return;

        if (!PersistentData.contains(itemStack, "furniture_identifier")) return;

        Furniture furniture = furnitureModule.getFurniture(itemStack);
        if (furniture == null) return;
        event.setCancelled(true);

        Location location = block.getLocation();
        location.setYaw(this.getClosestYaw(player.getLocation().getYaw()));
        location.add(0.5D, 0D, 0.5D);

        Location blockLocation = location.clone().getBlock().getRelative(BlockFace.UP).getLocation();
        if (location.getBlock().getType() == Material.BARRIER) return;
        if (blockLocation.getBlock().getType() != Material.AIR) return;
        if (furniture.getFurnitureSize() == FurnitureSize.LARGE && blockLocation.clone().add(0, 1, 0).getBlock().getType() != Material.AIR) return;

        furniture.placeBlock(location);

        if (player.getGameMode() != GameMode.CREATIVE) {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
        cooldownCache.asMap().put(player.getUniqueId(), System.currentTimeMillis());
    }

    protected float getClosestYaw(float yaw) {
        float newYaw = yaw;
        if (yaw >= 80 && yaw <= 100) newYaw = 90;
        else if (yaw >= -100 && yaw <= -80) newYaw = -90;
        else if (yaw >= -10 && yaw <= 10) newYaw = 0;
        else if (yaw >= 170 && yaw <= 180) newYaw = 180;
        else if (yaw >= -180 && yaw <= -170) newYaw = -180;
        return newYaw + 180;
    }
}