package com.jazzkuh.furniture.modules.furniture.commands;

import com.jazzkuh.commandlib.common.annotations.Alias;
import com.jazzkuh.commandlib.common.annotations.Command;
import com.jazzkuh.commandlib.common.annotations.Greedy;
import com.jazzkuh.commandlib.common.annotations.Main;
import com.jazzkuh.commandlib.common.annotations.Permission;
import com.jazzkuh.commandlib.common.annotations.Subcommand;
import com.jazzkuh.commandlib.spigot.AnnotationCommand;
import com.jazzkuh.furniture.utils.ChatUtils;
import com.jazzkuh.furniture.utils.ItemBuilder;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

@Command("itemmodifier")
public final class ItemModifierCommand extends AnnotationCommand {

    @Main
    @Alias("im")
    @Permission("core.commands.itemmodifier")
    public void onMain(Player player) {
        this.formatUsage(player);
    }

    @Subcommand("rename")
    @Permission("core.commands.itemmodifier.rename")
    public void rename(Player player, @Greedy String name) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType() == Material.AIR) {
            player.sendMessage(ChatUtils.prefix("Items", "<error>You have to hold an item to modify it."));
            return;
        }

        ItemBuilder builder = new ItemBuilder(stack);
        builder.setName(name);

        player.getInventory().setItemInMainHand(builder.toItemStack());
        player.sendMessage(ChatUtils.prefix("Items", "<gray>Renamed item to <white>%1<gray>.", name));
    }

    @Subcommand("addlore")
    @Permission("core.commands.itemmodifier.lore")
    public void addLore(Player player, @Greedy String lore) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType() == Material.AIR) {
            player.sendMessage(ChatUtils.prefix("Items", "<error>You have to hold an item to modify it."));
            return;
        }

        ItemBuilder builder = new ItemBuilder(stack);
        builder.addLoreLine(lore);

        player.getInventory().setItemInMainHand(builder.toItemStack());
        player.sendMessage(ChatUtils.prefix("Items", "<gray>Added lore to item."));
    }

    @Subcommand("removeLore")
    @Permission("core.commands.itemmodifier.lore")
    public void removeLore(Player player, @Greedy Integer index) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType() == Material.AIR) {
            player.sendMessage(ChatUtils.prefix("Items", "<error>You have to hold an item to modify it."));
            return;
        }

        ItemBuilder builder = new ItemBuilder(stack);
        builder.removeLoreLine(index);

        player.getInventory().setItemInMainHand(builder.toItemStack());
        player.sendMessage(ChatUtils.prefix("Items", "<gray>Removed lore from item."));
    }

    @Subcommand("hideflags")
    @Permission("core.commands.itemmodifier.hideflags")
    public void hideFlags(Player player) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType() == Material.AIR) {
            player.sendMessage(ChatUtils.prefix("Items", "<error>You have to hold an item to modify it."));
            return;
        }

        ItemBuilder builder = new ItemBuilder(stack);
        builder.setItemFlag(ItemFlag.values());

        player.getInventory().setItemInMainHand(builder.toItemStack());
        player.sendMessage(ChatUtils.prefix("Items", "<gray>All flags hidden."));
    }

    @Subcommand("unbreakable")
    @Permission("core.commands.itemmodifier.unbreakable")
    public void unbreakable(Player player, Boolean unbreakable) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType() == Material.AIR) {
            player.sendMessage(ChatUtils.prefix("Items", "<error>You have to hold an item to modify it."));
            return;
        }

        ItemBuilder builder = new ItemBuilder(stack);
        builder.makeUnbreakable(unbreakable);

        player.getInventory().setItemInMainHand(builder.toItemStack());
        player.sendMessage(ChatUtils.prefix("Items", "<gray>Item is now <white>%1<gray>.", (unbreakable ? "unbreakable" : "breakable")));
    }

    @Subcommand("color")
    @Permission("core.commands.itemmodifier.color")
    public void color(Player player, String hexColor) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType() == Material.AIR) {
            player.sendMessage(ChatUtils.prefix("Items", "<error>You have to hold an item to modify it."));
            return;
        }

        TextColor color = TextColor.fromHexString(hexColor);
        if (color == null) {
            player.sendMessage(ChatUtils.prefix("Items", "<error>Invalid color."));
            return;
        }

        ItemBuilder builder = new ItemBuilder(stack);
        builder.setColor(Color.fromRGB(color.value()));

        player.getInventory().setItemInMainHand(builder.toItemStack());
        player.sendMessage(ChatUtils.prefix("Items", "<gray>Item color set."));
    }

    @Subcommand("skullowner")
    @Permission("core.commands.itemmodifier.skullowner")
    public void skullOwner(Player player, Player target) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType() == Material.AIR) {
            player.sendMessage(ChatUtils.prefix("Items", "<error>You have to hold an item to modify it."));
            return;
        }

        ItemBuilder builder = new ItemBuilder(stack);
        builder.setSkullOwner(target);

        player.getInventory().setItemInMainHand(builder.toItemStack());
        player.sendMessage(ChatUtils.prefix("Items", "<gray>Skull owner set."));
    }

    @Subcommand("custommodeldata")
    @Permission("core.commands.itemmodifier.custommodeldata")
    public void customModelData(Player player, Integer data) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType() == Material.AIR) {
            player.sendMessage(ChatUtils.prefix("Items", "<error>You have to hold an item to modify it."));
            return;
        }

        ItemBuilder builder = new ItemBuilder(stack);
        builder.setCustomModelData(data);

        player.getInventory().setItemInMainHand(builder.toItemStack());
        player.sendMessage(ChatUtils.prefix("Items", "<gray>Custom model data set."));
    }

    @Subcommand("itemmodel")
    @Permission("core.commands.itemmodifier.itemmodel")
    public void itemModel(Player player, String model) {
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType() == Material.AIR) {
            player.sendMessage(ChatUtils.prefix("Items", "<error>You have to hold an item to modify it."));
            return;
        }

        ItemBuilder builder = new ItemBuilder(stack);
        builder.itemModel(model);

        player.getInventory().setItemInMainHand(builder.toItemStack());
        player.sendMessage(ChatUtils.prefix("Items", "<gray>Item model set."));
    }
}