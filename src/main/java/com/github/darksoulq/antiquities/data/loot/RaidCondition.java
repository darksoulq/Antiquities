package com.github.darksoulq.antiquities.data.loot;

import com.github.darksoulq.abyssallib.common.serialization.Codec;
import com.github.darksoulq.abyssallib.world.data.loot.LootCondition;
import com.github.darksoulq.abyssallib.world.data.loot.LootConditionType;
import com.github.darksoulq.abyssallib.world.data.loot.LootContext;
import org.bukkit.Raid;
import org.bukkit.entity.Raider;

public class RaidCondition extends LootCondition {
    public static final Codec<RaidCondition> CODEC = Codec.unit(RaidCondition::new);
    public static final LootConditionType<RaidCondition> TYPE = () -> CODEC;

    @Override
    public boolean test(LootContext context) {
        if (context.victim() instanceof Raider raider && raider.getRaid() != null) {
            return true;
        }

        if (context.location() != null && context.location().getWorld().hasRaids()) {
            Raid raid = context.location().getWorld().locateNearestRaid(context.location(), 112);
            return raid != null && raid.getStatus() == Raid.RaidStatus.ONGOING;
        }

        return false;
    }

    @Override
    public LootConditionType<?> getType() {
        return TYPE;
    }
}