package com.github.darksoulq.antiquities.data;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import com.github.darksoulq.abyssallib.server.event.SubscribeEvent;
import com.github.darksoulq.abyssallib.world.item.Item;
import com.github.darksoulq.antiquities.Antiquities;
import com.github.darksoulq.antiquities.item.RelicItem;
import com.github.darksoulq.relique.api.RelicAPI;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Events {

    @SubscribeEvent
    public void onServerLoad(ServerLoadEvent event) {
        if (event.getType() != ServerLoadEvent.LoadType.STARTUP) return;
        new Pack(JavaPlugin.getPlugin(Antiquities.class));
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerQuitEvent event) {
        VexTracker.despawnAll(event.getPlayer().getUniqueId());
    }

    @SubscribeEvent
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof LivingEntity target)) return;

        for (RelicAPI.SlotResult result : RelicAPI.getAllEquipped(target)) {
            Item resolved = Item.resolve(result.item());
            if (resolved instanceof RelicItem relicItem) {
                relicItem.onTarget(event, result);
            }
        }
    }

    @SubscribeEvent
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof LivingEntity attacker)) return;
        if (!(event.getEntity() instanceof LivingEntity victim)) return;

        for (RelicAPI.SlotResult result : RelicAPI.getAllEquipped(attacker)) {
            Item resolved = Item.resolve(result.item());
            if (resolved instanceof RelicItem relicItem) {
                relicItem.onAttack(event, result);
            }
        }

        for (RelicAPI.SlotResult result : RelicAPI.getAllEquipped(victim)) {
            Item resolved = Item.resolve(result.item());
            if (resolved instanceof RelicItem relicItem) {
                relicItem.onHurt(event, result);
            }
        }

        VexTracker.retarget(attacker.getUniqueId(), victim);
        VexTracker.retarget(victim.getUniqueId(), attacker);
    }

    @SubscribeEvent
    public void onMove(PlayerMoveEvent event) {
        if (!event.hasChangedBlock()) return;

        for (RelicAPI.SlotResult result : RelicAPI.getAllEquipped(event.getPlayer())) {
            Item resolved = Item.resolve(result.item());
            if (resolved instanceof RelicItem relicItem) {
                relicItem.onMove(event, result);
            }
        }
    }

    @SubscribeEvent
    public void onDropItem(PlayerDropItemEvent event) {
        for (RelicAPI.SlotResult result : RelicAPI.getAllEquipped(event.getPlayer())) {
            Item resolved = Item.resolve(result.item());
            if (resolved instanceof RelicItem relicItem) {
                relicItem.onDrop(event, result);
                if (event.isCancelled()) return;
            }
        }
    }

    @SubscribeEvent
    public void onKnockback(EntityKnockbackByEntityEvent event) {
        for (RelicAPI.SlotResult result : RelicAPI.getAllEquipped(event.getEntity())) {
            Item resolved = Item.resolve(result.item());
            if (resolved instanceof RelicItem relicItem) {
                relicItem.onKnockback(event, result);
            }
        }
    }

    @SubscribeEvent
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        for (RelicAPI.SlotResult result : RelicAPI.getAllEquipped(entity)) {
            Item resolved = Item.resolve(result.item());
            if (resolved instanceof RelicItem relicItem) {
                relicItem.onPotionEffect(event, result);
            }
        }
    }
}