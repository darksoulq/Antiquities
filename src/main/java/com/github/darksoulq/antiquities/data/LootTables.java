package com.github.darksoulq.antiquities.data;


import com.github.darksoulq.abyssallib.world.data.loot.LootLoader;
import com.github.darksoulq.abyssallib.world.data.loot.LootTable;
import com.github.darksoulq.antiquities.Antiquities;

import com.github.darksoulq.abyssallib.server.registry.DeferredRegistry;
import com.github.darksoulq.abyssallib.server.registry.Registries;
import org.bukkit.plugin.java.JavaPlugin;

public class LootTables {
    public static final DeferredRegistry<LootTable> LOOT_TABLES = DeferredRegistry.create(Registries.LOOT_TABLES, Antiquities.PLUGIN_ID);

    public static final LootTable WARDEN = LOOT_TABLES.register("warden", _ ->
        LootLoader.loadResource(JavaPlugin.getPlugin(Antiquities.class), "loot_tables/warden.json"));
    public static final LootTable EVOKER = LOOT_TABLES.register("evoker", _ ->
        LootLoader.loadResource(JavaPlugin.getPlugin(Antiquities.class), "loot_tables/evoker.json"));
    public static final LootTable STRIDER = LOOT_TABLES.register("strider", _ ->
        LootLoader.loadResource(JavaPlugin.getPlugin(Antiquities.class), "loot_tables/strider.json"));
    public static final LootTable WITCH = LOOT_TABLES.register("witch", _ ->
        LootLoader.loadResource(JavaPlugin.getPlugin(Antiquities.class), "loot_tables/witch.json"));
    public static final LootTable PIGLIN_BARTERING = LOOT_TABLES.register("piglin_bartering", _ ->
        LootLoader.loadResource(JavaPlugin.getPlugin(Antiquities.class), "loot_tables/piglin_bartering.json"));
    public static final LootTable FISHING_TREASURE = LOOT_TABLES.register("fishing/treasure", _ ->
        LootLoader.loadResource(JavaPlugin.getPlugin(Antiquities.class), "loot_tables/fishing_treasure.json"));
    public static final LootTable END_CITY_TREASURE = LOOT_TABLES.register("chests/end_city_treasure", _ ->
        LootLoader.loadResource(JavaPlugin.getPlugin(Antiquities.class), "loot_tables/end_city.json"));
    public static final LootTable BLAZE = LOOT_TABLES.register("blaze", _ ->
        LootLoader.loadResource(JavaPlugin.getPlugin(Antiquities.class), "loot_tables/blaze.json"));
}