package com.github.darksoulq.antiquities;

import com.github.darksoulq.abyssallib.server.cooldown.Cooldown;
import com.github.darksoulq.abyssallib.server.event.EventBus;
import com.github.darksoulq.abyssallib.server.scheduler.Clock;
import com.github.darksoulq.abyssallib.server.scheduler.Scheduler;
import com.github.darksoulq.abyssallib.world.advancement.AdvancementLoader;
import com.github.darksoulq.abyssallib.world.data.tag.TagLoader;
import com.github.darksoulq.abyssallib.world.recipe.RecipeLoader;
import com.github.darksoulq.antiquities.data.*;
import com.github.darksoulq.antiquities.data.loot.LootExtras;
import com.github.darksoulq.antiquities.item.Items;
import org.bukkit.plugin.java.JavaPlugin;

public final class Antiquities extends JavaPlugin {
    public static final String PLUGIN_ID = "antiquities";

    public EventBus bus;
    private Scheduler scheduler;
    private Cooldown cooldown;

    @Override
    public void onEnable() {
        bus = new EventBus(this);
        scheduler = new Scheduler(this);
        cooldown = new Cooldown(scheduler, Clock.TICKS);

        Items.ITEMS.apply();
        LootExtras.CONDITIONS.apply();
        LootTables.LOOT_TABLES.apply();
        TagLoader.loadFolder(this, "tags");
        RecipeLoader.loadFolder(this, "recipes");
        Advancements.ADVANCEMENTS.apply();

        bus.register(new Events());
    }

    @Override
    public void onDisable() {
        VexTracker.despawnAll();
    }

    public Cooldown getCooldown() {
        return cooldown;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}