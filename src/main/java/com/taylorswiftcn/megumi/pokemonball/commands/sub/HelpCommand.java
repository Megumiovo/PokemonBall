package com.taylorswiftcn.megumi.pokemonball.commands.sub;

import com.taylorswiftcn.megumi.pokemonball.commands.WeiCommand;
import com.taylorswiftcn.megumi.pokemonball.file.sub.Message;
import org.bukkit.command.CommandSender;

public class HelpCommand extends WeiCommand {

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        Message.Help.forEach(CommandSender::sendMessage);
        if (CommandSender.hasPermission("pokemonball.admin"))
            Message.AdminHelp.forEach(CommandSender::sendMessage);
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
