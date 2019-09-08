package com.taylorswiftcn.megumi.pokemonball.file.sub;

import com.taylorswiftcn.megumi.pokemonball.PokemonBall;
import com.taylorswiftcn.megumi.pokemonball.util.WeiUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class Message {
    private static YamlConfiguration message;

    public static List<String> Help;
    public static List<String> AdminHelp;

    public static String NoPermission;
    public static String needSlot;
    public static String convertEggSuccess;
    public static String convertPokemonSuccess;

    public static void init() {
        message = PokemonBall.getInstance().getFileManager().getMessage();

        Help = getStringList("Help");
        AdminHelp = getStringList("AdminHelp");

        NoPermission = getString("Message.NoPermission");
        needSlot = getString("Message.NeedSlot");
        convertEggSuccess = getString("Message.ConvertEggSuccess");
        convertPokemonSuccess = getString("Message.ConvertPokemonSuccess");
    }

    private static String getString(String path) {
        return WeiUtil.onReplace(message.getString(path));
    }

    private static List<String> getStringList(String path) {
        return WeiUtil.onReplace(message.getStringList(path));
    }


}
