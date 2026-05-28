package com.github.darksoulq.antiquities.item;

import com.github.darksoulq.abyssallib.server.cooldown.CooldownResult;
import com.github.darksoulq.abyssallib.server.cooldown.CooldownScope;
import com.github.darksoulq.abyssallib.server.scheduler.Clock;
import com.github.darksoulq.abyssallib.server.scheduler.TimeUnit;
import com.github.darksoulq.abyssallib.world.item.component.builtin.CooldownUse;
import com.github.darksoulq.abyssallib.world.item.component.builtin.Lore;
import com.github.darksoulq.abyssallib.world.item.component.builtin.Rarity;
import com.github.darksoulq.abyssallib.world.particle.Particles;
import com.github.darksoulq.abyssallib.world.particle.impl.Generators;
import com.github.darksoulq.abyssallib.world.particle.impl.Renderers;
import com.github.darksoulq.antiquities.Antiquities;
import com.github.darksoulq.antiquities.data.Advancements;
import com.github.darksoulq.antiquities.data.VexTracker;
import com.github.darksoulq.relique.api.RelicAPI;
import com.github.darksoulq.relique.component.RelicAttributeModifier;
import com.github.darksoulq.relique.component.RelicProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class EvokersMantle extends RelicItem {
    public EvokersMantle(Key id) {
        super(id);
        setData(new RelicProperties(List.of(
            new RelicAttributeModifier(
                Attribute.MAX_HEALTH.key(),
                Key.key(Antiquities.PLUGIN_ID, "evokers_mantle"),
                1,
                AttributeModifier.Operation.ADD_NUMBER,
                List.of("chest")
            )
        )));
        setData(new Rarity(ItemRarity.EPIC));
        setData(new CooldownUse(UseCooldown.useCooldown(30.0f).cooldownGroup(Key.key(Antiquities.PLUGIN_ID, "evokers_mantle")).build()));
        setData(new Lore(List.of(
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("<gray><!i>Taking damage that drops you below"),
            MiniMessage.miniMessage().deserialize("<red><!i>30% Max Health</red><gray><!i> summons <aqua><!i>2 Vexes</aqua>"),
            MiniMessage.miniMessage().deserialize("<gray><!i>that protect you for <gold><!i>15 seconds</gold><gray><!i>."),
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("<dark_gray><!i>Cooldown: 30s")
        )));
    }

    @Override
    public void onTarget(EntityTargetEvent event, RelicAPI.SlotResult slot) {
        if (!(event.getEntity() instanceof Vex vex)) return;

        NamespacedKey ownerKey = new NamespacedKey(Antiquities.PLUGIN_ID, "vex_owner");
        if (!vex.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) return;

        String ownerUuid = vex.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
        if (ownerUuid == null) return;
        if (event.getTarget() == null) return;
        if (!ownerUuid.equals(event.getTarget().getUniqueId().toString())) return;

        event.setCancelled(true);
    }

    @Override
    public void onHurt(EntityDamageByEntityEvent event, RelicAPI.SlotResult slot) {
        if (!(event.getEntity() instanceof LivingEntity victim)) return;
        if (!(event.getDamager() instanceof LivingEntity attacker)) return;

        AttributeInstance maxHealthAttr = victim.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthAttr == null) return;

        if ((victim.getHealth() - event.getFinalDamage()) >= (maxHealthAttr.getValue() * 0.3)) return;

        Antiquities plugin = JavaPlugin.getPlugin(Antiquities.class);
        CooldownResult cdResult = plugin.getCooldown().acquire(
            CooldownScope.entity(victim),
            Key.key(Antiquities.PLUGIN_ID, "evokers_mantle"),
            600,
            TimeUnit.TICKS
        );

        if (!cdResult.isReady()) return;

        NamespacedKey ownerKey = new NamespacedKey(Antiquities.PLUGIN_ID, "vex_owner");
        String victimUuid = victim.getUniqueId().toString();

        for (int i = 0; i < 2; i++) {
            victim.getWorld().spawn(victim.getLocation().add(0, 1, 0), Vex.class, v -> {
                v.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, victimUuid);
                v.setTarget(attacker);

                VexTracker.add(victim.getUniqueId(), v);

                v.getWorld().playSound(v.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 1.0f, 1.0f);

                Particles.builder()
                    .origin(() -> v.getLocation().add(0, 0.5, 0))
                    .shape(Generators.circle(1.2, 20))
                    .render(new Renderers.Standard(Particle.WITCH, 1, 0, null))
                    .duration(20)
                    .build().start();

                plugin.getScheduler().schedule(() -> VexTracker.despawn(v)).entity(v).after(300, Clock.TICKS).once();
            });
        }

        if (victim instanceof Player p) {
            Advancements.EVOKERS_MANTLE.getProgress(p).awardCriterion("impossible");
            p.setCooldown(slot.item(), 600);
        }
    }
}