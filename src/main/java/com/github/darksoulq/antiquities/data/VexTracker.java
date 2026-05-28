package com.github.darksoulq.antiquities.data;

import com.github.darksoulq.abyssallib.world.particle.Particles;
import com.github.darksoulq.abyssallib.world.particle.impl.Generators;
import com.github.darksoulq.abyssallib.world.particle.impl.Renderers;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vex;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class VexTracker {
    private static final Map<UUID, List<Vex>> TRACKED = new ConcurrentHashMap<>();

    public static void add(UUID owner, Vex vex) {
        TRACKED.computeIfAbsent(owner, k -> new CopyOnWriteArrayList<>()).add(vex);
    }

    public static void retarget(UUID owner, LivingEntity target) {
        List<Vex> vexes = TRACKED.get(owner);
        if (vexes == null || vexes.isEmpty()) return;

        for (Vex vex : vexes) {
            if (!vex.isValid() || vex.isDead()) {
                vexes.remove(vex);
                continue;
            }
            vex.setTarget(target);
        }
    }

    public static void despawn(Vex vex) {
        if (vex == null || !vex.isValid() || vex.isDead()) return;

        vex.getWorld().playSound(vex.getLocation(), Sound.ENTITY_VEX_DEATH, 1.0f, 1.0f);
        Particles.builder()
            .origin(vex.getLocation().add(0, 0.5, 0))
            .shape(Generators.sphere(0.8, 30))
            .render(new Renderers.Standard(Particle.SMOKE, 1, 0.05, null))
            .duration(5)
            .build().start();

        vex.remove();
    }

    public static void despawnAll(UUID owner) {
        List<Vex> vexes = TRACKED.remove(owner);
        if (vexes == null) return;
        for (Vex vex : vexes) despawn(vex);
    }

    public static void despawnAll() {
        for (UUID owner : TRACKED.keySet()) despawnAll(owner);
        TRACKED.clear();
    }
}