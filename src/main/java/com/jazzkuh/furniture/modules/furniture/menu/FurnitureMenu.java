package com.jazzkuh.furniture.modules.furniture.menu;

import com.jazzkuh.furniture.modules.furniture.FurnitureModule;
import com.jazzkuh.furniture.modules.furniture.models.FurnitureModel;
import com.jazzkuh.furniture.utils.ChatUtils;
import com.jazzkuh.furniture.utils.ItemBuilder;
import com.jazzkuh.inventorylib.objects.PaginatedMenu;
import com.jazzkuh.inventorylib.objects.icon.Icon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;


public class FurnitureMenu extends PaginatedMenu {
	private final FurnitureModule furnitureModule;

	public FurnitureMenu(FurnitureModule furnitureModule) {
		super(ChatUtils.format("Furniture"), 6);
		this.furnitureModule = furnitureModule;
		this.registerPageSlotsBetween(0, 45);

		List<FurnitureModel> sorted = new java.util.ArrayList<>(furnitureModule.getFurnitureModels().values());
		sorted.sort(Comparator.comparing(FurnitureModel::getKey));

		for (FurnitureModel furnitureModel : sorted) {
			ItemStack itemStack = new ItemBuilder(furnitureModel.getItemStack()).toItemStack();
			this.addItem(new Icon(itemStack, event -> player.getInventory().addItem(itemStack)));
		}
	}

	@Override
	public Icon getPreviousPageItem() {
		return new Icon(48, new ItemBuilder(Material.STRUCTURE_VOID)
				.setName("<success>Vorige Pagina")
				.addLoreLine("<gray>Ga naar de vorige pagina.")
				.addLoreLine("")
				.addLoreLine("<icon_left> <dark_gray>= <gray>Navigeer")
				.setCustomModelData(2)
				.toItemStack());
	}

	@Override
	public Icon getNextPageItem() {
		return new Icon(50, new ItemBuilder(Material.STRUCTURE_VOID)
				.setName("<success>Volgende Pagina")
				.addLoreLine("<gray>Ga naar de volgende pagina.")
				.addLoreLine("")
				.addLoreLine("<icon_left> <dark_gray>= <gray>Navigeer")
				.setCustomModelData(3)
				.toItemStack());
	}
}
