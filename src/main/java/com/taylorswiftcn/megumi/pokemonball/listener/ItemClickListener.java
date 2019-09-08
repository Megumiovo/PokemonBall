package com.taylorswiftcn.megumi.pokemonball.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.taylorswiftcn.megumi.pokemonball.file.sub.Config;
import com.taylorswiftcn.megumi.pokemonball.file.sub.Message;
import com.taylorswiftcn.megumi.pokemonball.util.ItemUtil;
import com.taylorswiftcn.megumi.pokemonball.util.PokemonConvertUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemClickListener implements Listener {

    @EventHandler
    public void onRight(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Action action = e.getAction();

        if (ItemUtil.isEmpty(item)) return;
        if (!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) return;

        Pokemon pokemon = PokemonConvertUtil.asPokemon(item);
        if (pokemon == null) return;

        item.setAmount(item.getAmount() - 1);
        player.getInventory().setItemInMainHand(item);

        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueId());
        storage.add(pokemon);

        player.sendMessage(Config.Prefix + Message.convertPokemonSuccess);
    }
}
