package com.github.darksoulq.antiquities.item;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import com.github.darksoulq.abyssallib.world.item.Item;
import com.github.darksoulq.abyssallib.world.item.component.builtin.MaxStackSize;
import com.github.darksoulq.relique.api.RelicAPI;
import net.kyori.adventure.key.Key;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public abstract class RelicItem extends Item {
    public RelicItem(Key id) {
        super(id);
        setData(new MaxStackSize(1));
    }

    public void onTarget(EntityTargetEvent event, RelicAPI.SlotResult slot) {}

    public void onAttack(EntityDamageByEntityEvent event, RelicAPI.SlotResult slot) {}

    public void onHurt(EntityDamageByEntityEvent event, RelicAPI.SlotResult slot) {}

    public void onMove(PlayerMoveEvent event, RelicAPI.SlotResult slot) {}

    public void onDrop(PlayerDropItemEvent event, RelicAPI.SlotResult slot) {}

    public void onKnockback(EntityKnockbackByEntityEvent event, RelicAPI.SlotResult slot) {}

    public void onPotionEffect(EntityPotionEffectEvent event, RelicAPI.SlotResult slot) {}
}