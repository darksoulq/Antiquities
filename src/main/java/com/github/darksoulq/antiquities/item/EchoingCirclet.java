package com.github.darksoulq.antiquities.item;

import com.github.darksoulq.abyssallib.server.cooldown.CooldownResult;
import com.github.darksoulq.abyssallib.server.cooldown.CooldownScope;
import com.github.darksoulq.abyssallib.server.scheduler.TimeUnit;
import com.github.darksoulq.abyssallib.world.item.component.builtin.CooldownUse;
import com.github.darksoulq.abyssallib.world.item.component.builtin.Lore;
import com.github.darksoulq.abyssallib.world.item.component.builtin.Rarity;
import com.github.darksoulq.antiquities.Antiquities;
import com.github.darksoulq.relique.api.RelicAPI;
import com.github.darksoulq.relique.component.RelicAttributeModifier;
import com.github.darksoulq.relique.component.RelicProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class EchoingCirclet extends RelicItem {
    public EchoingCirclet(Key id) {
        super(id);
        setData(new RelicProperties(List.of(
            new RelicAttributeModifier(
                Attribute.ARMOR_TOUGHNESS.key(),
                Key.key(Antiquities.PLUGIN_ID, "echoing_circlet"),
                2,
                AttributeModifier.Operation.ADD_NUMBER,
                List.of("head")
            )
        )));
        setData(new Rarity(ItemRarity.EPIC));
        setData(new CooldownUse(UseCooldown.useCooldown(10f).cooldownGroup(Key.key(Antiquities.PLUGIN_ID, "echoing_circlet")).build()));
        setData(new Lore(List.of(
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("<gray><!i>Attacking enemies inflicts them with"),
            MiniMessage.miniMessage().deserialize("<yellow><!i>Glowing</yellow><gray><!i> and <dark_gray><!i>Blindness</dark_gray><gray><!i> for <gold><!i>5 seconds</gold><gray><!i>."),
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("<dark_gray><!i>Cooldown: 10s")
        )));
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent event, RelicAPI.SlotResult slot) {
        if (!(event.getEntity() instanceof LivingEntity victim) || !(event.getDamager() instanceof LivingEntity attacker)) return;

        CooldownResult cdResult = JavaPlugin.getPlugin(Antiquities.class).getCooldown().acquire(
            CooldownScope.entity(attacker),
            Key.key(Antiquities.PLUGIN_ID, "echoing_circlet"),
            200,
            TimeUnit.TICKS
        );

        if (cdResult.isReady()) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
            if (attacker instanceof Player p) {
                p.setCooldown(slot.item(), 200);
            }
        }
    }
}