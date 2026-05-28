package com.github.darksoulq.antiquities.item;

import com.github.darksoulq.abyssallib.server.cooldown.CooldownResult;
import com.github.darksoulq.abyssallib.server.cooldown.CooldownScope;
import com.github.darksoulq.abyssallib.server.event.EventBus;
import com.github.darksoulq.abyssallib.server.scheduler.TimeUnit;
import com.github.darksoulq.abyssallib.world.item.component.builtin.CooldownUse;
import com.github.darksoulq.abyssallib.world.item.component.builtin.Lore;
import com.github.darksoulq.abyssallib.world.item.component.builtin.Rarity;
import com.github.darksoulq.antiquities.Antiquities;
import com.github.darksoulq.antiquities.data.Advancements;
import com.github.darksoulq.relique.api.RelicAPI;
import com.github.darksoulq.relique.component.RelicAttributeModifier;
import com.github.darksoulq.relique.component.RelicProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class WitchsBrewstone extends RelicItem {
    public WitchsBrewstone(Key id) {
        super(id);
        setData(new RelicProperties(List.of(
            new RelicAttributeModifier(
                Attribute.LUCK.key(),
                Key.key(Antiquities.PLUGIN_ID, "witchs_brewstone"),
                2,
                AttributeModifier.Operation.ADD_NUMBER,
                List.of("charm")
            )
        )));
        setData(new Rarity(ItemRarity.EPIC));
        setData(new CooldownUse(UseCooldown.useCooldown(30.0f).cooldownGroup(Key.key(Antiquities.PLUGIN_ID, "witchs_brewstone")).build()));
        setData(new Lore(List.of(
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("<gray><!i>Purifies incoming <dark_green><!i>Poison</dark_green><gray><!i>, <dark_gray><!i>Wither</dark_gray><gray><!i>, or"),
            MiniMessage.miniMessage().deserialize("<gray><!i>Slowness</gray><gray><!i> effects into <light_purple><!i>Regeneration I</light_purple>"),
            MiniMessage.miniMessage().deserialize("<gray><!i>for <gold><!i>5 seconds</gold><gray><!i>."),
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("<dark_gray><!i>Cooldown: 30s")
        )));
    }

    @Override
    public void onPotionEffect(EntityPotionEffectEvent event, RelicAPI.SlotResult slot) {
        EntityPotionEffectEvent.Action action = event.getAction();
        if (action != EntityPotionEffectEvent.Action.ADDED && action != EntityPotionEffectEvent.Action.CHANGED) return;

        PotionEffect newEffect = event.getNewEffect();
        if (newEffect == null) return;

        PotionEffectType type = newEffect.getType();
        if (!type.equals(PotionEffectType.POISON) && !type.equals(PotionEffectType.WITHER) && !type.equals(PotionEffectType.SLOWNESS)) return;
        if (!(event.getEntity() instanceof LivingEntity victim)) return;

        Antiquities plugin = JavaPlugin.getPlugin(Antiquities.class);
        CooldownResult cdResult = plugin.getCooldown().acquire(
            CooldownScope.entity(victim),
            Key.key(Antiquities.PLUGIN_ID, "witchs_brewstone"),
            600,
            TimeUnit.TICKS
        );

        if (!cdResult.isReady()) return;

        event.setCancelled(true);
        victim.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));

        if (victim instanceof Player p) {
            Advancements.WITCHS_BREWSTONE.getProgress(p).awardCriterion("impossible");
            p.setCooldown(slot.item(), 600);
        }
    }
}