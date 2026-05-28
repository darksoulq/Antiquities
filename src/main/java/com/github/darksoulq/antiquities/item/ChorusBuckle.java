package com.github.darksoulq.antiquities.item;

import com.github.darksoulq.abyssallib.server.cooldown.CooldownResult;
import com.github.darksoulq.abyssallib.server.cooldown.CooldownScope;
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
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ChorusBuckle extends RelicItem {
    public ChorusBuckle(Key id) {
        super(id);
        setData(new RelicProperties(List.of(
            new RelicAttributeModifier(
                Attribute.ATTACK_SPEED.key(),
                Key.key(Antiquities.PLUGIN_ID, "chorus_buckle"),
                0.5,
                AttributeModifier.Operation.ADD_NUMBER,
                List.of("belt")
            )
        )));
        setData(new Rarity(ItemRarity.EPIC));
        setData(new CooldownUse(UseCooldown.useCooldown(15f).cooldownGroup(Key.key(Antiquities.PLUGIN_ID, "chorus_buckle")).build()));
        setData(new Lore(List.of(
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("<gray><!i>Pressing <white><bold><!i>Sneak + Drop</bold></white><gray><!i> triggers"),
            MiniMessage.miniMessage().deserialize("<gray><!i>a safe random teleport within a"),
            MiniMessage.miniMessage().deserialize("<aqua><!i>16-block</aqua><gray><!i> radius."),
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("<dark_gray><!i>Cooldown: 15s")
        )));
    }

    @Override
    public void onDrop(PlayerDropItemEvent event, RelicAPI.SlotResult slot) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        Antiquities plugin = JavaPlugin.getPlugin(Antiquities.class);
        CooldownResult cdResult = plugin.getCooldown().acquire(
            CooldownScope.player(player),
            Key.key(Antiquities.PLUGIN_ID, "chorus_buckle"),
            300,
            TimeUnit.TICKS
        );

        if (!cdResult.isReady()) return;
        Advancements.CHORUS_BUCKLE_USE.getProgress(player).awardCriterion("impossible");

        Location loc = player.getLocation();
        World world = loc.getWorld();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < 16; i++) {
            double x = loc.getX() + (random.nextDouble() - 0.5) * 16.0;
            double targetY = loc.getY() + (random.nextInt(16) - 8);
            double y = Math.max(world.getMinHeight(), Math.min(world.getMaxHeight() - 1, targetY));
            double z = loc.getZ() + (random.nextDouble() - 0.5) * 16.0;

            Location target = new Location(world, x, y, z, loc.getYaw(), loc.getPitch());

            while (target.getY() > world.getMinHeight() && !target.getBlock().getType().isSolid()) {
                target.subtract(0, 1, 0);
            }

            Block ground = target.getBlock();
            if (!ground.getType().isSolid() || ground.isLiquid()) continue;

            Block feet = target.clone().add(0, 1, 0).getBlock();
            Block head = target.clone().add(0, 2, 0).getBlock();

            if (!feet.isPassable() || feet.isLiquid() || !head.isPassable() || head.isLiquid()) continue;

            target.add(0, 1, 0);

            event.setCancelled(true);

            world.playSound(loc, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1.0f, 1.0f);
            player.teleport(target, PlayerTeleportEvent.TeleportCause.CONSUMABLE_EFFECT);
            world.playSound(target, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1.0f, 1.0f);

            player.setCooldown(slot.item(), 300);
            return;
        }

        plugin.getCooldown().reset(CooldownScope.player(player), Key.key(Antiquities.PLUGIN_ID, "chorus_buckle"));
    }
}