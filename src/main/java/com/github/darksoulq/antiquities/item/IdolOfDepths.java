package com.github.darksoulq.antiquities.item;

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
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemRarity;

import java.util.List;

public class IdolOfDepths extends RelicItem {
    public IdolOfDepths(Key id) {
        super(id);
        setData(new RelicProperties(List.of(
            new RelicAttributeModifier(
                Attribute.WATER_MOVEMENT_EFFICIENCY.key(),
                Key.key(Antiquities.PLUGIN_ID, "idol_of_depths"),
                0.1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1,
                List.of("charm")
            )
        )));
        setData(new Rarity(ItemRarity.RARE));
        setData(new Lore(List.of(
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("<gray><!i>Taking damage while submerged in"),
            MiniMessage.miniMessage().deserialize("<aqua><!i>Water</aqua><gray><!i> retaliates with <light_purple><!i>2.0 Magic</light_purple>"),
            MiniMessage.miniMessage().deserialize("<light_purple><!i>Damage</light_purple><gray><!i> to the attacker.")
        )));
    }

    @Override
    public void onHurt(EntityDamageByEntityEvent event, RelicAPI.SlotResult slot) {
        if (!(event.getEntity() instanceof LivingEntity victim) || !(event.getDamager() instanceof LivingEntity attacker)) return;

        if (victim.isInWater()) {
            attacker.damage(2.0, DamageSource.builder(DamageType.MAGIC)
                .withCausingEntity(victim)
                .build()
            );
        }
    }
}