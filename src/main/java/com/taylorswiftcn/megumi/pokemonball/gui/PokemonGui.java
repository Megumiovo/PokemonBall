package com.taylorswiftcn.megumi.pokemonball.gui;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.taylorswiftcn.megumi.pokemonball.file.sub.Config;
import com.taylorswiftcn.megumi.pokemonball.util.ItemUtil;
import com.taylorswiftcn.megumi.pokemonball.util.PokemonConvertUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PokemonGui {

    private Inventory gui;

    public void open(Player player) {
        gui = Bukkit.createInventory(null, 45, Config.Gui.title);
        addContent(player);
        player.openInventory(gui);
    }

    private void addContent(Player player) {
        for (int i = 0; i < 45; i++) {
            gui.setItem(i, ItemUtil.createItem("160", 1, 1, " ", null));
        }

        int[] slot = {10, 13, 16, 28, 31, 34};
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueId());
        for (int i = 0; i < 6; i++) {
            Pokemon pokemon = storage.get(i);

            if (pokemon == null) {
                gui.setItem(slot[i], Config.Gui.emptySlot);
                continue;
            }

            if (pokemon.getPixelmonIfExists() != null) {
                gui.setItem(slot[i], Config.Gui.notExists);
                continue;
            }

            ItemStack item = PokemonConvertUtil.asItem(pokemon, false);

            gui.setItem(slot[i], item);
        }
    }

    private ItemStack setTag(ItemStack itemStack) {
        ItemStack item = getBukkitItemStack(itemStack.clone());
        NbtCompound nbt = NbtFactory.asCompound(NbtFactory.fromItemTag(item));

        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        nbt.put("SpriteName", builder.append("pixelmon:sprites/pokemon/").append(random.nextInt(300)).toString());
        NbtFactory.setItemTag(item, nbt);

        return item;
    }

    private static ItemStack getBukkitItemStack(ItemStack item) {
        if (item == null) {
            return new ItemStack(Material.AIR);
        }
        return !item.getClass().getName().endsWith("CraftItemStack") ? MinecraftReflection.getBukkitItemStack(item) : item;
    }
}
