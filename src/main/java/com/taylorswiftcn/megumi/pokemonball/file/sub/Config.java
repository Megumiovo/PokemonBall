package com.taylorswiftcn.megumi.pokemonball.file.sub;

import com.taylorswiftcn.megumi.pokemonball.PokemonBall;
import com.taylorswiftcn.megumi.pokemonball.util.ItemUtil;
import com.taylorswiftcn.megumi.pokemonball.util.WeiUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Config {
    private static YamlConfiguration config;

    public static String Prefix;

    public static String pokemon_id;
    public static Integer pokemon_data;
    public static String pokemon_name;
    public static List<String> pokemon_lore;
    public static List<String> skillFormat;
    public static String asEgg;
    public static String asPokemon;

    public static class Gui {
        public static String title;
        public static ItemStack emptySlot;
        public static ItemStack notExists;

    }

    public static void init() {
        config = PokemonBall.getInstance().getFileManager().getConfig();

        Prefix = getString("Prefix");
        skillFormat = config.getStringList("Config.SkillFormat");
        pokemon_id = config.get("Config.Pokemon.ID").toString();
        pokemon_data = config.getInt("Config.Pokemon.Data");
        pokemon_name = config.getString("Config.Pokemon.Name");
        pokemon_lore = config.getStringList("Config.Pokemon.Lore");
        asEgg = config.getString("Config.AsEgg");
        asPokemon = config.getString("Config.AsPokemon");

        Gui.title = getString("Gui.Title");
        Gui.emptySlot = getButton("EmptySlot");
        Gui.notExists = getButton("NotExists");
    }

    private static String getString(String path) {
        return WeiUtil.onReplace(config.getString(path));
    }

    private static List<String> getStringList(String path) {
        return WeiUtil.onReplace(config.getStringList(path));
    }

    private static ItemStack getButton(String s) {
        return ItemUtil.createItem(
                config.get(String.format("Gui.%s.ID", s)).toString(),
                config.getInt(String.format("Gui.%s.Data", s)),
                1,
                config.getString(String.format("Gui.%s.Name", s)),
                config.getStringList(String.format("Gui.%s.Lore", s))
        );
    }
}
