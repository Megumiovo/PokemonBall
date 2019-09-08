package com.taylorswiftcn.megumi.pokemonball.listener;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.taylorswiftcn.megumi.pokemonball.PokemonBall;
import com.taylorswiftcn.megumi.pokemonball.file.sub.Config;
import com.taylorswiftcn.megumi.pokemonball.file.sub.Message;
import com.taylorswiftcn.megumi.pokemonball.util.ItemUtil;
import com.taylorswiftcn.megumi.pokemonball.util.PokemonConvertUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class InventoryClickListener implements Listener {

    private PokemonBall plugin = PokemonBall.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (!e.getInventory().getTitle().equals(Config.Gui.title)) return;

        Player player = (Player) e.getWhoClicked();
        Inventory gui = e.getInventory();

        ItemStack item = e.getCurrentItem();
        int rawSlot = e.getRawSlot();
        ClickType click = e.getClick();

        if (ItemUtil.isEmpty(item)) return;

        e.setCancelled(true);

        List<Integer> slots = Arrays.asList(10, 13, 16, 28, 31, 34);
        if (!slots.contains(rawSlot)) return;
        if (click != ClickType.RIGHT) return;

        if (item.equals(Config.Gui.emptySlot)) return;
        if (item.equals(Config.Gui.notExists)) return;

        int slot = slots.indexOf(rawSlot);

        givePokemonBall(player, slot);

        plugin.getPokemonGui().open(player);
    }

    private void givePokemonBall(Player player, int index) {
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueId());
        Pokemon pokemon = storage.get(index);
        if (pokemon == null) return;

        int size = ItemUtil.getEmptySize(player);

        ItemStack item = PokemonConvertUtil.asItem(pokemon, true);
        ItemStack heldItem = MinecraftReflection.getBukkitItemStack(pokemon.getHeldItem());

        if (heldItem.getType() == Material.AIR) {
            if (size < 1) {
                player.sendMessage(Config.Prefix + Message.needSlot.replace("%s%", "1"));
                return;
            }
        }
        else {
            if (size < 2) {
                player.sendMessage(Config.Prefix + Message.needSlot.replace("%s%", "2"));
                return;
            }
        }

        storage.set(index, null);

        player.getInventory().addItem(item);
        player.getInventory().addItem(heldItem);

        player.sendMessage(Config.Prefix + Message.convertEggSuccess);
    }
}
