package com.taylorswiftcn.megumi.pokemonball.commands.sub;

import com.taylorswiftcn.megumi.pokemonball.PokemonBall;
import com.taylorswiftcn.megumi.pokemonball.commands.WeiCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommand extends WeiCommand {

    private PokemonBall plugin = PokemonBall.getInstance();

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        Player player = getPlayer();
        plugin.getPokemonGui().open(player);
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return "pokemonball.use";
    }
}
