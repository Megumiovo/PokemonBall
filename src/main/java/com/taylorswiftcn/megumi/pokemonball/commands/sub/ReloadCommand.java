package com.taylorswiftcn.megumi.pokemonball.commands.sub;

import com.taylorswiftcn.megumi.pokemonball.PokemonBall;
import com.taylorswiftcn.megumi.pokemonball.commands.WeiCommand;
import com.taylorswiftcn.megumi.pokemonball.file.sub.Config;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends WeiCommand {

    private PokemonBall plugin = PokemonBall.getInstance();

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        plugin.reload();
        CommandSender.sendMessage(Config.Prefix + "§c重载成功!");
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return "pokemonball.admin";
    }
}
