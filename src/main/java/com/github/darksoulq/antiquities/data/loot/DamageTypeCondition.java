package com.github.darksoulq.antiquities.data.loot;

import com.github.darksoulq.abyssallib.common.serialization.Codec;
import com.github.darksoulq.abyssallib.common.serialization.Codecs;
import com.github.darksoulq.abyssallib.common.serialization.RecordBuilder;
import com.github.darksoulq.abyssallib.world.data.loot.LootCondition;
import com.github.darksoulq.abyssallib.world.data.loot.LootConditionType;
import com.github.darksoulq.abyssallib.world.data.loot.LootContext;
import net.kyori.adventure.key.Key;
import org.bukkit.Raid;
import org.bukkit.entity.Raider;

public class DamageTypeCondition extends LootCondition {
    public static final Codec<DamageTypeCondition> CODEC = RecordBuilder.create(instance -> instance.group(
        Codecs.KEY.fieldOf("id").forGetter(DamageTypeCondition.class, c -> c.id)
    ).apply(instance, DamageTypeCondition::new));

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