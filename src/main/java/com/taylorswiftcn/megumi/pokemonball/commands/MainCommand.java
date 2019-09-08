package com.taylorswiftcn.megumi.pokemonball.commands;

import com.taylorswiftcn.megumi.pokemonball.commands.sub.HelpCommand;
import com.taylorswiftcn.megumi.pokemonball.commands.sub.OpenCommand;
import com.taylorswiftcn.megumi.pokemonball.commands.sub.ReloadCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class MainCommand implements CommandExecutor {
    private HelpCommand help;
    private HashMap<String, WeiCommand> commands;

    public MainCommand() {
        this.help = new HelpCommand();
        this.commands = new HashMap<>();
        this.commands.put("open", new OpenCommand());
        this.commands.put("reload", new ReloadCommand());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        WeiCommand cmd = help;
        if (strings.length >= 1 && commands.containsKey(strings[0])) {
            cmd = commands.get(strings[0]);
        }
        cmd.execute(commandSender, strings);
        return false;
    }
}
