package com.github.darksoulq.antiquities.item;

import com.github.darksoulq.abyssallib.world.item.component.builtin.Lore;
import com.github.darksoulq.abyssallib.world.item.component.builtin.Rarity;
import com.github.darksoulq.antiquities.Antiquities;
import com.github.darksoulq.relique.api.RelicAPI;
import com.github.darksoulq.relique.component.RelicAttributeModifier;
import com.github.darksoulq.relique.component.RelicProperties;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class MembraneCowl extends RelicItem {
    private static final Key MODIFIER_KEY = Key.key(Antiquities.PLUGIN_ID, "membrane_cowl");
    private static final NamespacedKey AGGRO_KEY = new NamespacedKey(Antiquities.PLUGIN_ID, "cowl_aggro");

    public MembraneCowl(Key id) {
        super(id);
        setData(new RelicProperties(List.of(
            new RelicAttributeModifier(
                Attribute.MOVEMENT_SPEED.key(),
                Key.key(Antiquities.PLUGIN_ID, "membrane_cowl"),
                0.02,
                AttributeModifier.Operation.ADD_NUMBER,
                List.of("head")
            )
        )));
        setData(new Rarity(ItemRarity.RARE));
        setData(new Lore(List.of(
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("<dark_blue><!i>Phantoms</dark_blue><gray><!i> remain completely neutral"),
            MiniMessage.miniMessage().deserialize("<gray><!i>and will not swoop unless provoked.")
        )));
    }

    @Override
    public void onTarget(EntityTargetEvent event, RelicAPI.SlotResult slot) {
        if (!(event.getEntity() instanceof Phantom phantom)) return;
        if (event.getTarget() == null) return;

        EntityTargetEvent.TargetReason reason = event.getReason();

        if (reason == EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY ||
            reason == EntityTargetEvent.TargetReason.TARGET_ATTACKED_NEARBY_ENTITY ||
            reason == EntityTargetEvent.TargetReason.CUSTOM ||
            reason == EntityTargetEvent.TargetReason.UNKNOWN) {

            phantom.getPersistentDataContainer().set(AGGRO_KEY, PersistentDataType.STRING, event.getTarget().getUniqueId().toString());
            return;
        }

        if (phantom.getPersistentDataContainer().has(AGGRO_KEY, PersistentDataType.STRING)) {
            String uuid = phantom.getPersistentDataContainer().get(AGGRO_KEY, PersistentDataType.STRING);
            if (uuid != null && uuid.equals(event.getTarget().getUniqueId().toString())) {
                return;
            }
        }

        event.setCancelled(true);
        event.setTarget(null);
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event, RelicAPI.SlotResult slot) {
        if (!(event.getEntity() instanceof Phantom phantom)) return;

        LivingEntity attacker = null;
        if (event.getDamager() instanceof LivingEntity entity) {
            attacker = entity;
        } else if (event.getDamager() instanceof Projectile proj && proj.getShooter() instanceof LivingEntity shooter) {
            attacker = shooter;
        }

        if (attacker != null) {
            phantom.getPersistentDataContainer().set(AGGRO_KEY, PersistentDataType.STRING, attacker.getUniqueId().toString());
            phantom.setTarget(attacker);
        }
    }
}