package com.github.darksoulq.antiquities.item;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import com.github.darksoulq.abyssallib.world.item.component.builtin.Lore;
import com.github.darksoulq.abyssallib.world.item.component.builtin.Rarity;
import com.github.darksoulq.antiquities.Antiquities;
import com.github.darksoulq.relique.api.RelicAPI;
import com.github.darksoulq.relique.component.RelicAttributeModifier;
import com.github.darksoulq.relique.component.RelicProperties;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Ravager;
import org.bukkit.inventory.ItemRarity;

import java.util.List;

public class HeartOfVillage extends RelicItem {
    public HeartOfVillage(Key id) {
        super(id);
        setData(new RelicProperties(List.of(
            new RelicAttributeModifier(
                Attribute.MAX_HEALTH.key(),
                Key.key(Antiquities.PLUGIN_ID, "heart_of_village"),
                4,
                AttributeModifier.Operation.ADD_NUMBER,
                List.of("chest")
            ),
            new RelicAttributeModifier(
                Attribute.KNOCKBACK_RESISTANCE.key(),
                Key.key(Antiquities.PLUGIN_ID, "heart_of_village"),
                0.4,
                AttributeModifier.Operation.ADD_NUMBER,
                List.of("chest")
            ),
            new RelicAttributeModifier(
                Attribute.MOVEMENT_SPEED.key(),
                Key.key(Antiquities.PLUGIN_ID, "heart_of_village"),
                -0.02,
                AttributeModifier.Operation.ADD_NUMBER,
                List.of("chest")
            )
        )));
        setData(new Rarity(ItemRarity.EPIC));
        setData(new Lore(List.of(
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("<gray><!i>Grants total immunity to the launch"),
            MiniMessage.miniMessage().deserialize("<gray><!i>knockback attacks of <white><!i>Iron Golems</white>"),
            MiniMessage.miniMessage().deserialize("<gray><!i>and <dark_green><!i>Ravagers</dark_green><gray><!i>.")
        )));
    }

    @Override
    public void onKnockback(EntityKnockbackByEntityEvent event, RelicAPI.SlotResult slot) {
        if (event.getHitBy() instanceof IronGolem || event.getHitBy() instanceof Ravager) {
            event.setCancelled(true);
        }
    }
}