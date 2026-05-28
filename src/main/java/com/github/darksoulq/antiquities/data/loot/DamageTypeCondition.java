package com.github.darksoulq.antiquities.data.loot;

import com.github.darksoulq.abyssallib.common.serialization.Codec;
import com.github.darksoulq.abyssallib.common.serialization.Codecs;
import com.github.darksoulq.abyssallib.common.serialization.DynamicOps;
import com.github.darksoulq.abyssallib.world.data.loot.LootCondition;
import com.github.darksoulq.abyssallib.world.data.loot.LootConditionType;
import com.github.darksoulq.abyssallib.world.data.loot.LootContext;
import net.kyori.adventure.key.Key;
import org.bukkit.Raid;
import org.bukkit.entity.Raider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DamageTypeCondition extends LootCondition {
    public static final Codec<DamageTypeCondition> CODEC = new Codec<>() {
        @Override
        public <D> DamageTypeCondition decode(DynamicOps<D> ops, D input) {
            Map<D, D> map = ops.getMap(input).orElseThrow(() -> new CodecException("Expected map"));
            Key id = Codecs.KEY.decode(ops, map.get(ops.createString("id")));
            return new DamageTypeCondition(id);
        }

        @Override
        public <D> D encode(DynamicOps<D> ops, DamageTypeCondition value) {
            Map<D, D> map = new HashMap<>();
            map.put(ops.createString("id"), Codecs.KEY.encode(ops, value.id));
            return ops.createMap(map);
        }
    };
    public static final LootConditionType<DamageTypeCondition> TYPE = () -> CODEC;

    private final Key id;

    public DamageTypeCondition(Key id) {
        this.id = id;
    }

    @Override
    public boolean test(LootContext context) {
        if (context.victim() == null) return false;
        if (context.victim().getLastDamageCause() == null) return false;
        return context.victim().getLastDamageCause().getDamageSource().getDamageType().key().equals(id);
    }

    @Override
    public LootConditionType<?> getType() {
        return TYPE;
    }
}