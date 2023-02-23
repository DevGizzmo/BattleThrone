package net.gizzmo.battlethrone.item;

import net.gizzmo.battlethrone.api.tools.StringTools;
import net.gizzmo.battlethrone.api.tools.item.ItemsTools;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AxeItem {
    private final ItemStack itemStack;

    public AxeItem(final String name, final List<String> lore) {
        this.itemStack = ItemsTools.createItemStack(Material.WOOD_AXE, StringTools.fixColors(name), StringTools.fixColorsList(lore));
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }
}
