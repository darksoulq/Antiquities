package com.github.darksoulq.antiquities.item;

import com.github.darksoulq.abyssallib.world.item.component.builtin.Lore;
import com.github.darksoulq.abyssallib.world.item.component.builtin.Rarity;
import com.github.darksoulq.relique.api.RelicAPI;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BlazeEmberRing extends RelicItem {
    public BlazeEmberRing(Key id) {
        super(id);
        setData(new Rarity(ItemRarity.RARE));
        setData(new Lore(List.of(
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("<gray><!i>Unarmed attacks or attacks with"),
            MiniMessage.miniMessage().deserialize("<gray><!i>non-enchantable items ignite the"),
            MiniMessage.miniMessage().deserialize("<gray><!i>target for <gold><!i>4 seconds</gold><gray><!i>.")
        )));
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event, RelicAPI.SlotResult slot) {
        if (!(event.getEntity() instanceof LivingEntity victim) || !(event.getDamager() instanceof LivingEntity attacker)) return;

        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK && cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;

        EntityEquipment equipment = attacker.getEquipment();
        if (equipment == null) return;

        ItemStack weapon = equipment.getItemInMainHand();
        if (weapon.getType().getMaxDurability() > 0) return;

        victim.setFireTicks(Math.max(victim.getFireTicks(), 80));
    }
}