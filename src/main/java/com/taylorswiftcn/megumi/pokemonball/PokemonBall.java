package com.taylorswiftcn.megumi.pokemonball;

import com.taylorswiftcn.megumi.pokemonball.commands.MainCommand;
import com.taylorswiftcn.megumi.pokemonball.file.FileManager;
import com.taylorswiftcn.megumi.pokemonball.file.sub.Config;
import com.taylorswiftcn.megumi.pokemonball.file.sub.Message;
import com.taylorswiftcn.megumi.pokemonball.gui.PokemonGui;
import com.taylorswiftcn.megumi.pokemonball.listener.InventoryClickListener;
import com.taylorswiftcn.megumi.pokemonball.listener.ItemClickListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class PokemonBall extends JavaPlugin {
    @Getter private static PokemonBall instance;

    @Getter private FileManager fileManager;

    @Getter private PokemonGui pokemonGui;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        instance = this;

        fileManager = new FileManager(this);
        pokemonGui = new PokemonGui();
        fileManager.init();

        Config.init();
        Message.init();

        getCommand("pb").setExecutor(new MainCommand());
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new ItemClickListener(), this);

        long end = System.currentTimeMillis();

        getLogger().info("加载成功! 用时 %time% ms".replace("%time%", String.valueOf(end - start)));
    }

    @Override
    public void onDisable() {
        closeInventory();
        getLogger().info("卸载成功!");
    }

    public String getVersion() {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        return packet.substring(packet.lastIndexOf('.') + 1);
    }

    public void reload() {
        closeInventory();
        fileManager.init();
        Config.init();
        Message.init();
    }

    private void closeInventory() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory() == null) continue;
            if (player.getOpenInventory().getTitle().equals(Config.Gui.title)) {
                player.closeInventory();
            }
        }
    }
}
