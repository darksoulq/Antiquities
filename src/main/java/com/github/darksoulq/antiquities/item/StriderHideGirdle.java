package com.github.darksoulq.antiquities.item;

import com.github.darksoulq.abyssallib.server.scheduler.Clock;
import com.github.darksoulq.abyssallib.world.item.component.builtin.Lore;
import com.github.darksoulq.abyssallib.world.item.component.builtin.Rarity;
import com.github.darksoulq.antiquities.Antiquities;
import com.github.darksoulq.antiquities.data.Advancements;
import com.github.darksoulq.relique.api.RelicAPI;
import com.github.darksoulq.relique.component.RelicAttributeModifier;
import com.github.darksoulq.relique.component.RelicProperties;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class StriderHideGirdle extends RelicItem {
    private static final int RADIUS = 3;
    private static final int RADIUS_SQ = RADIUS * RADIUS;

    public StriderHideGirdle(Key id) {
        super(id);
        setData(new RelicProperties(List.of(
            new RelicAttributeModifier(
                Attribute.ARMOR.key(),
                Key.key(Antiquities.PLUGIN_ID, "strider_hide_girdle"),
                2,
                AttributeModifier.Operation.ADD_NUMBER,
                List.of("belt")
            )
        )));
        setData(new Rarity(ItemRarity.EPIC));
        setData(new Lore(List.of(
            MiniMessage.miniMessage().deserialize(""),
            MiniMessage.miniMessage().deserialize("<gray><!i>Walking on <red><!i>Lava</red><gray><!i> temporarily freezes"),
            MiniMessage.miniMessage().deserialize("<gray><!i>a <dark_gray><!i>3-block radius</dark_gray><gray><!i> into <gray><!i>Basalt</gray><gray><!i> that"),
            MiniMessage.miniMessage().deserialize("<gray><!i>melts after <gold><!i>6 seconds</gold><gray><!i>.")
        )));
    }

    @Override
    public void onMove(PlayerMoveEvent event, RelicAPI.SlotResult slot) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
            event.getFrom().getBlockY() == event.getTo().getBlockY() &&
            event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        Player player = event.getPlayer();
        if (player.getVehicle() != null || !player.isOnGround()) return;

        Block start = player.getLocation().getBlock();
        Antiquities plugin = JavaPlugin.getPlugin(Antiquities.class);
        boolean basaltFormed = false;

        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int z = -RADIUS; z <= RADIUS; z++) {
                if (x * x + z * z > RADIUS_SQ) continue;

                Block b = start.getRelative(x, -1, z);
                if (b.getType() != Material.LAVA) continue;

                Block above = b.getRelative(0, 1, 0);
                if (!above.getType().isAir()) continue;

                if (!(b.getBlockData() instanceof Levelled level)) continue;
                if (level.getLevel() != 0) continue;

                BlockState state = b.getState();
                state.setType(Material.BASALT);

                EntityBlockFormEvent formEvent = new EntityBlockFormEvent(player, b, state);
                Bukkit.getPluginManager().callEvent(formEvent);

                if (formEvent.isCancelled()) continue;

                formEvent.getNewState().update(true);
                basaltFormed = true;

                plugin.getScheduler().schedule(() -> {
                    if (b.getType() != Material.BASALT) return;

                    b.setType(Material.MAGMA_BLOCK);

                    plugin.getScheduler().schedule(() -> {
                        if (b.getType() == Material.MAGMA_BLOCK) {
                            b.setType(Material.LAVA);
                        }
                    }).region(b.getLocation()).after(60, Clock.TICKS).once();

                }).region(b.getLocation()).after(60, Clock.TICKS).once();
            }
        }

        if (basaltFormed) {
            Advancements.STRIDER_GIRDLE.getProgress(player).awardCriterion("impossible");
        }
    }
}