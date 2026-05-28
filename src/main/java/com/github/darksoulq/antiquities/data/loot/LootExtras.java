package com.github.darksoulq.antiquities.data.loot;

import com.github.darksoulq.abyssallib.server.registry.DeferredRegistry;
import com.github.darksoulq.abyssallib.server.registry.Registries;
import com.github.darksoulq.abyssallib.world.data.loot.LootConditionType;
import com.github.darksoulq.antiquities.Antiquities;

public final class LootExtras {
    public static final DeferredRegistry<LootConditionType<?>> CONDITIONS = DeferredRegistry.create(Registries.LOOT_CONDITIONS, Antiquities.PLUGIN_ID);

    public static final LootConditionType<?> IS_RAID = CONDITIONS.register("is_raid", _ -> RaidCondition.TYPE);
    public static final LootConditionType<?> DAMAGE_TYPE = CONDITIONS.register("damage_type", _ -> DamageTypeCondition.TYPE);
}
