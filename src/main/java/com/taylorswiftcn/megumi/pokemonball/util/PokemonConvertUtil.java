package com.taylorswiftcn.megumi.pokemonball.util;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.entities.pixelmon.EnumSpecialTexture;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.taylorswiftcn.megumi.pokemonball.PokemonBall;
import com.taylorswiftcn.megumi.pokemonball.file.sub.Config;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PokemonConvertUtil {

    private static PokemonBall plugin = PokemonBall.getInstance();

    public static ItemStack asItem(Pokemon pokemon, boolean nbt) {
        String id = Config.pokemon_id;
        int data = Config.pokemon_data;
        String name = Config.pokemon_name.replace("%displayname%", pokemon.getDisplayName());
        List<String> lore = new ArrayList<>();

        Moveset moveset = pokemon.getMoveset();
        IVStore ivs = pokemon.getIVs();
        EVStore evs = pokemon.getEVs();
        for (String s : Config.pokemon_lore) {
            if (s.contains("%skill%")) {
                for (int j = 0; j < 4; j++) {
                    Attack attack = moveset.get(j);
                    if (attack == null) continue;
                    String skillName = attack.baseAttack.getLocalizedName();
                    String description = attack.baseAttack.getLocalizedDescription();
                    for (String skill : Config.skillFormat) {
                        lore.add(skill
                                .replace("%skillname%", skillName)
                                .replace("%description%", description)
                        );
                    }
                }
                continue;
            }
            lore.add(s
                    .replace("%displayname%", pokemon.getDisplayName())
                    .replace("%name%", pokemon.getSpecies().getLocalizedName())
                    .replace("%shiny%", pokemon.isShiny() ? "是" : "否")
                    .replace("%level%", String.valueOf(pokemon.getLevel()))
                    .replace("%attack%", String.valueOf(ivs.attack))
                    .replace("%defence%", String.valueOf(ivs.defence))
                    .replace("%health%", String.valueOf(ivs.hp))
                    .replace("%specialattack%", String.valueOf(ivs.specialAttack))
                    .replace("%specialdefence%", String.valueOf(ivs.specialDefence))
                    .replace("%speed%", String.valueOf(ivs.speed))
                    .replace("%e_attack%", String.valueOf(evs.attack))
                    .replace("%e_defence%", String.valueOf(evs.defence))
                    .replace("%e_health%", String.valueOf(evs.hp))
                    .replace("%e_specialattack%", String.valueOf(evs.specialAttack))
                    .replace("%e_specialdefence%", String.valueOf(evs.specialDefence))
                    .replace("%e_speed%", String.valueOf(evs.speed))
                    .replace("%friendship%", String.valueOf(pokemon.getFriendship()))
                    .replace("%growth%", pokemon.getGrowth().getLocalizedName())
                    .replace("%gender%", pokemon.getGender().getLocalizedName())
                    .replace("%nature%", pokemon.getNature().getLocalizedName())
                    .replace("%ability%", pokemon.getAbility().getLocalizedName())
            );
        }

        if (nbt) {
            lore.add(Config.asPokemon);
        }
        else {
            lore.add(Config.asEgg);
        }

        ItemStack item = ItemUtil.createItem(id, data, 1, name, lore);

        item = writePhotoNbt(pokemon, item);

        if (nbt) {
            item = writeNbt(pokemon, item);
        }

        return item;
    }

    public static Pokemon asPokemon(ItemStack item) {
        item = getBukkitItemStack(item);
        NbtCompound nbt = NbtFactory.asCompound(NbtFactory.fromItemTag(item));

        if (!nbt.containsKey(POKEMONBALL_TAG)) {
            return null;
        }

        Pokemon pokemon = Pixelmon.pokemonFactory.create(EnumSpecies.getFromDex(nbt.getInteger(POKEMONBALL_TAG)));
        if (nbt.containsKey(FORM_TAG)) {
            try {
                pokemon.setForm(nbt.getByte(FORM_TAG));
            }
            catch (ClassCastException e) {
                pokemon.setForm(nbt.getInteger(FORM_TAG));
            }
        }
        if (nbt.containsKey(LEVEL_TAG)) pokemon.setLevel(nbt.getInteger(LEVEL_TAG));
        if (nbt.containsKey(EXP_TAG)) pokemon.setExperience(nbt.getInteger(EXP_TAG));
        if (nbt.containsKey(HEALTH_TAG)) pokemon.setHealth(nbt.getInteger(HEALTH_TAG));
        if (nbt.containsKey(SHINY_TAG)) pokemon.setShiny(nbt.getInteger(SHINY_TAG) != 0);
        if (nbt.containsKey(IVS_TAG)) pokemon.getStats().ivs.fillFromArray(nbt.getIntegerArray(IVS_TAG));
        if (nbt.containsKey(EVS_TAG)) pokemon.getStats().evs.fillFromArray(nbt.getIntegerArray(EVS_TAG));
        if (nbt.containsKey(FRIENDSHIP_TAG)) pokemon.setFriendship(nbt.getInteger(FRIENDSHIP_TAG));
        if (nbt.containsKey(GROWTH_TAG)) pokemon.setGrowth(EnumGrowth.getGrowthFromIndex(nbt.getInteger(GROWTH_TAG)));
        if (nbt.containsKey(GENDER_TAG)) pokemon.setGender(Gender.getGender(nbt.getString(GENDER_TAG)));
        if (nbt.containsKey(NATURE_TAG)) pokemon.setNature(EnumNature.getNatureFromIndex(nbt.getInteger(NATURE_TAG)));
        if (nbt.containsKey(ABILITY_TAG)) pokemon.setAbility(nbt.getString(ABILITY_TAG));
        if (nbt.containsKey(CAUGHTBALL_TAG)) pokemon.setCaughtBall(EnumPokeballs.getFromIndex(nbt.getInteger(CAUGHTBALL_TAG)));
        for (int i = 0; i < SKILLS_TAG.length; i++) {
            String s = SKILLS_TAG[i];
            if (!nbt.containsKey(s)) {
                pokemon.getMoveset().set(i, null);
                continue;
            }
            int[] array = nbt.getIntegerArray(s);
            Attack attack = new Attack(array[0]);
            attack.pp = array[1];
            attack.ppBase = array[2];
            pokemon.getMoveset().set(i, attack);
        }

        return pokemon;
    }

    private final static String POKEMONBALL_TAG = "POKEMONBALL";
    private final static String FORM_TAG = "FORM";
    private final static String LEVEL_TAG = "LEVEL";
    private final static String EXP_TAG = "EXP";
    private final static String HEALTH_TAG = "HEALTH";
    private final static String SHINY_TAG = "SHINY";
    private final static String IVS_TAG = "IVS";
    private final static String EVS_TAG = "EVS";
    private final static String FRIENDSHIP_TAG = "FRIENDSHIP";
    private final static String GROWTH_TAG = "GROWTH";
    private final static String GENDER_TAG = "GENDER";
    private final static String NATURE_TAG = "NATURE";
    private final static String ABILITY_TAG = "ABILITY";
    private final static String CAUGHTBALL_TAG = "CAUGHTBALL";
    private final static String[] SKILLS_TAG = {"SKILL1", "SKILL2", "SKILL3", "SKILL4"};

    private static ItemStack writeNbt(Pokemon pokemon, ItemStack itemStack) {
        ItemStack item = getBukkitItemStack(itemStack.clone());
        NbtCompound nbt = NbtFactory.asCompound(NbtFactory.fromItemTag(item));

        nbt.put(POKEMONBALL_TAG, pokemon.getSpecies().getNationalPokedexInteger());
        nbt.put(FORM_TAG, pokemon.getForm());
        nbt.put(LEVEL_TAG, pokemon.getLevel());
        nbt.put(EXP_TAG, pokemon.getExperience());
        nbt.put(HEALTH_TAG, pokemon.getHealth());
        nbt.put(SHINY_TAG, pokemon.isShiny() ? 1 : 0);
        nbt.put(IVS_TAG, pokemon.getStats().ivs.getArray());
        nbt.put(EVS_TAG, pokemon.getStats().evs.getArray());
        nbt.put(FRIENDSHIP_TAG, pokemon.getFriendship());
        nbt.put(GROWTH_TAG, pokemon.getGrowth().index);
        nbt.put(GENDER_TAG, pokemon.getGender().name());
        nbt.put(NATURE_TAG, pokemon.getNature().index);
        nbt.put(ABILITY_TAG, pokemon.getAbility().getName());
        nbt.put(CAUGHTBALL_TAG, pokemon.getCaughtBall().getIndex());
        Moveset moveset = pokemon.getMoveset();
        for (int i = 0; i < moveset.attacks.length; i++) {
            Attack attack = moveset.get(i);
            if (attack == null) continue;
            nbt.put(SKILLS_TAG[i], new int[] {attack.baseAttack.attackIndex, attack.pp, attack.ppBase});
        }

        NbtFactory.setItemTag(item, nbt);

        return item;
    }

    private static ItemStack writePhotoNbt(Pokemon pokemon, ItemStack itemStack) {
        /*String tag = "pixelmon:sprites/pokemon/" + pokemon.getSpecies().getNationalPokedexNumber();*/

        ItemStack item = getBukkitItemStack(itemStack.clone());
        NbtCompound nbt = NbtFactory.asCompound(NbtFactory.fromItemTag(item));

        /*nbt.put("SpriteName", tag);*/
        nbt.put("ndex", (short) pokemon.getSpecies().getNationalPokedexInteger());
        nbt.put("form", (byte) pokemon.getForm());
        nbt.put("gender", pokemon.getGender().getForm());
        nbt.put("Shiny", pokemon.isShiny() ? 1 : 0);
        if (pokemon.getSpecialTexture() != EnumSpecialTexture.None) {
            nbt.put("specialTexture", (byte) pokemon.getSpecialTexture().id);
        }

        if (pokemon.getNickname() != null && !pokemon.getNickname().isEmpty()) {
            nbt.put("Nickname", pokemon.getNickname());
        }

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
