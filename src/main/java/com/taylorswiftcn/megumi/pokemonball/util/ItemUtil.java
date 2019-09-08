package com.taylorswiftcn.megumi.pokemonball.util;

import com.taylorswiftcn.megumi.pokemonball.PokemonBall;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemUtil {
    private static PokemonBall plugin = PokemonBall.getInstance();

    public static boolean isEmpty(ItemStack item) {
        return item == null || item.getItemMeta() == null;
    }

    public static int getEmptySize(Player player) {
        int i = 0;
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (isEmpty(item)) i++;
        }

        return i;
    }

    public static ItemStack createItem(String id, int data, int amount) {
        return createItem(id, data, amount, null, null, null);
    }

    public static ItemStack createItem(String id, int data, int amount, String name, List<String> lore) {
        return createItem(id, data, amount, name, lore, null);
    }

    @SuppressWarnings("deprecation")
    public static ItemStack createItem(String id, int data, int amount, String name, List<String> lore, HashMap<Enchantment, Integer> enchants) {
        ItemStack item;
        if (WeiUtil.isNumber(id)) {
            if (plugin.getVersion().startsWith("V1_13")) return new ItemStack(Material.STONE, 1);
            item = new ItemStack(Material.getMaterial(Integer.parseInt(id)), amount);
        }
        else
            item = new ItemStack(Material.getMaterial(id), amount);
        item.setDurability((short) data);
        ItemMeta meta = item.getItemMeta();
        if (name != null)
            meta.setDisplayName(WeiUtil.onReplace(name));
        if (lore != null)
            meta.setLore(WeiUtil.onReplace(lore));
        if (enchants != null) {
            for (Map.Entry<Enchantment, Integer> map : enchants.entrySet()) {
                Enchantment enchantment = map.getKey();
                Integer level = map.getValue();
                meta.addEnchant(enchantment, level, false);
            }
        }
        item.setItemMeta(meta);
        return item;
    }
}
