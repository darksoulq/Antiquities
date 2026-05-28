package com.github.darksoulq.antiquities.item;

import com.github.darksoulq.abyssallib.server.registry.DeferredRegistry;
import com.github.darksoulq.abyssallib.server.registry.Registries;
import com.github.darksoulq.abyssallib.world.item.Item;
import com.github.darksoulq.antiquities.Antiquities;

public final class Items {
    public static final DeferredRegistry<Item> ITEMS = DeferredRegistry.create(Registries.ITEMS, Antiquities.PLUGIN_ID);

    public static final Item PIGLIN_SIGNET = ITEMS.register("piglin_signet", PiglinSignet::new);
    public static final Item ECHOING_CIRCLET = ITEMS.register("echoing_circlet", EchoingCirclet::new);
    public static final Item IDOL_OF_DEPTHS = ITEMS.register("idol_of_depths", IdolOfDepths::new);
    public static final Item EVOKERS_MANTLE = ITEMS.register("evokers_mantle", EvokersMantle::new);
    public static final Item STRIDER_HIDE_GIRDLE = ITEMS.register("strider_hide_girdle", StriderHideGirdle::new);
    public static final Item MEMBRANE_COWL = ITEMS.register("membrane_cowl", MembraneCowl::new);
    public static final Item CHORUS_BUCKLE = ITEMS.register("chorus_buckle", ChorusBuckle::new);
//    public static final Item HEART_OF_VILLAGE = ITEMS.register("heart_of_village", HeartOfVillage::new);
    public static final Item BLAZE_EMBER_RING = ITEMS.register("blaze_ember_ring", BlazeEmberRing::new);
    public static final Item WITCHS_BREWSTONE = ITEMS.register("witchs_brewstone", WitchsBrewstone::new);
}
