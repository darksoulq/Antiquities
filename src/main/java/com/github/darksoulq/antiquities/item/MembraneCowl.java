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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class MembraneCowl extends RelicItem {
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
        NamespacedKey aggroKey = new NamespacedKey(Antiquities.PLUGIN_ID, "cowl_aggro");

        if (reason == EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY ||
            reason == EntityTargetEvent.TargetReason.TARGET_ATTACKED_NEARBY_ENTITY ||
            reason == EntityTargetEvent.TargetReason.CUSTOM ||
            reason == EntityTargetEvent.TargetReason.UNKNOWN) {

            phantom.getPersistentDataContainer().set(aggroKey, PersistentDataType.STRING, event.getTarget().getUniqueId().toString());
            return;
        }

        if (phantom.getPersistentDataContainer().has(aggroKey, PersistentDataType.STRING)) {
            String uuid = phantom.getPersistentDataContainer().get(aggroKey, PersistentDataType.STRING);
            if (uuid != null && uuid.equals(event.getTarget().getUniqueId().toString())) {
                return;
            }
        }

        event.setCancelled(true);
        event.setTarget(null);
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event, RelicAPI.SlotResult slot) {
        if (event.getEntity() instanceof Phantom phantom && event.getDamager() instanceof LivingEntity attacker) {
            NamespacedKey aggroKey = new NamespacedKey(Antiquities.PLUGIN_ID, "cowl_aggro");
            phantom.getPersistentDataContainer().set(aggroKey, PersistentDataType.STRING, attacker.getUniqueId().toString());
            phantom.setTarget(attacker);
        }
    }
}