package com.github.darksoulq.antiquities.data;

import com.github.darksoulq.abyssallib.server.registry.DeferredRegistry;
import com.github.darksoulq.abyssallib.server.registry.Registries;
import com.github.darksoulq.abyssallib.world.advancement.Advancement;
import com.github.darksoulq.abyssallib.world.advancement.AdvancementDisplay;
import com.github.darksoulq.abyssallib.world.advancement.AdvancementFrame;
import com.github.darksoulq.abyssallib.world.advancement.criterion.AutoGrantCriterion;
import com.github.darksoulq.abyssallib.world.advancement.criterion.ItemCraftedCriterion;
import com.github.darksoulq.abyssallib.world.advancement.criterion.ItemHasCriterion;
import com.github.darksoulq.abyssallib.world.advancement.criterion.StatisticCriterion;
import com.github.darksoulq.abyssallib.world.advancement.reward.ExperienceReward;
import com.github.darksoulq.abyssallib.world.item.ItemPredicate;
import com.github.darksoulq.antiquities.Antiquities;
import com.github.darksoulq.antiquities.item.Items;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Statistic;
import org.bukkit.potion.PotionEffectType;

public final class Advancements {
    public static final DeferredRegistry<Advancement> ADVANCEMENTS = DeferredRegistry.create(Registries.ADVANCEMENTS, Antiquities.PLUGIN_ID);

    public static final Advancement ROOT = ADVANCEMENTS.register("root", id -> Advancement.builder(id)
        .criterion("auto", new AutoGrantCriterion())
        .display(AdvancementDisplay.builder()
            .title(Component.text("Relics of the Past"))
            .description(Component.text("Discover a world of forgotten power."))
            .icon(Items.IDOL_OF_DEPTHS.getStack())
            .frame(AdvancementFrame.GOAL)
            .background(Key.key("minecraft", "gui/advancements/backgrounds/stone"))
            .build()
        )
        .build()
    );
//    public static final Advancement HEART_OF_VILlAGE = ADVANCEMENTS.register("heart_of_village", id -> Advancement.builder(id)
//        .parent(ROOT.getId())
//        .display(AdvancementDisplay.builder()
//            .title(Component.text("The Unmovable Object"))
//            .description(Component.text("Obtain the Heart of the Village."))
//            .frame(AdvancementFrame.TASK)
//            .icon(Items.HEART_OF_VILLAGE.getStack())
//            .build()
//        )
//        .criterion("has_relic", new ItemHasCriterion(ItemPredicate.builder().id(Items.HEART_OF_VILLAGE.getId()).build()))
//        .build()
//    );
    public static final Advancement WITCHS_BREWSTONE = ADVANCEMENTS.register("witchs_brewstone", id -> Advancement.builder(id)
        .parent(ROOT.getId())
        .display(AdvancementDisplay.builder()
            .title(Component.text("Potion Master's Bane"))
            .description(Component.text("Purify a deadly affliction into Regeneration using the Witch's Brewstone."))
            .frame(AdvancementFrame.TASK)
            .icon(Items.WITCHS_BREWSTONE.getStack())
            .build()
        )
        .build()
    );
    public static final Advancement MEMBRANE_COWL = ADVANCEMENTS.register("membrane_cowl", id -> Advancement.builder(id)
        .parent(ROOT.getId())
        .display(AdvancementDisplay.builder()
            .title(Component.text("Up All Night"))
            .description(Component.text("Craft a cowl from the membranes of Phantoms."))
            .frame(AdvancementFrame.TASK)
            .icon(Items.MEMBRANE_COWL.getStack())
            .build()
        )
        .criterion("crafted_relic", new ItemCraftedCriterion(Items.MEMBRANE_COWL.getId(), 1))
        .build()
    );
    public static final Advancement EVOKERS_MANTLE = ADVANCEMENTS.register("evokers_mantle", id -> Advancement.builder(id)
        .parent(ROOT.getId())
        .display(AdvancementDisplay.builder()
            .title(Component.text("Final Stand"))
            .description(Component.text("Drop below 30% health and summon protective Vexes."))
            .frame(AdvancementFrame.TASK)
            .icon(Items.EVOKERS_MANTLE.getStack())
            .build()
        )
        .build()
    );
    public static final Advancement PIGLIN_SIGNET = ADVANCEMENTS.register("piglin_signet", id -> Advancement.builder(id)
        .parent(ROOT.getId())
        .display(AdvancementDisplay.builder()
            .title(Component.text("Honor Among Thieves"))
            .description(Component.text("Obtain the Piglin Signet to walk safely among the horde."))
            .frame(AdvancementFrame.TASK)
            .icon(Items.PIGLIN_SIGNET.getStack())
            .build()
        )
        .criterion("has_relic", new ItemHasCriterion(ItemPredicate.builder().id(Items.PIGLIN_SIGNET.getId()).build()))
        .build()
    );
    public static final Advancement STRIDER_GIRDLE = ADVANCEMENTS.register("strider_hide_girdle", id -> Advancement.builder(id)
        .parent(ROOT.getId())
        .display(AdvancementDisplay.builder()
            .title(Component.text("Cold Feet"))
            .description(Component.text("Freeze the lava beneath your feet using the Strider Hide Girdle."))
            .frame(AdvancementFrame.TASK)
            .icon(Items.STRIDER_HIDE_GIRDLE.getStack())
            .build()
        )
        .build()
    );
    public static final Advancement BLAZE_EMBER_RING = ADVANCEMENTS.register("blaze_ember_ring", id -> Advancement.builder(id)
        .parent(ROOT.getId())
        .display(AdvancementDisplay.builder()
            .title(Component.text("Playing with Fire"))
            .description(Component.text("Slay a Blaze with your bare hands to earn the Blaze Ember Ring."))
            .frame(AdvancementFrame.TASK)
            .icon(Items.BLAZE_EMBER_RING.getStack())
            .build()
        )
        .criterion("has_relic", new ItemHasCriterion(ItemPredicate.builder().id(Items.BLAZE_EMBER_RING.getId()).build()))
        .build()
    );
    public static final Advancement IDOL_OF_DEPTHS = ADVANCEMENTS.register("idol_of_depths", id -> Advancement.builder(id)
        .parent(ROOT.getId())
        .display(AdvancementDisplay.builder()
            .title(Component.text("Sunken Treasure"))
            .description(Component.text("Fish up the ancient Idol of the Depths."))
            .frame(AdvancementFrame.TASK)
            .icon(Items.IDOL_OF_DEPTHS.getStack())
            .build()
        )
        .criterion("has_relic", new ItemHasCriterion(ItemPredicate.builder().id(Items.IDOL_OF_DEPTHS.getId()).build()))
        .build()
    );
    public static final Advancement ECHOING_CIRCLET = ADVANCEMENTS.register("echoing_circlet", id -> Advancement.builder(id)
        .parent(ROOT.getId())
        .display(AdvancementDisplay.builder()
            .title(Component.text("The Blind Leading the Blind"))
            .description(Component.text("Plunder the Echoing Circlet by slaying a Warden."))
            .frame(AdvancementFrame.TASK)
            .icon(Items.ECHOING_CIRCLET.getStack())
            .build()
        )
        .criterion("has_relic", new ItemHasCriterion(ItemPredicate.builder().id(Items.ECHOING_CIRCLET.getId()).build()))
        .build()
    );
    public static final Advancement CHORUS_BUCKLE_OBTAIN = ADVANCEMENTS.register("chorus_buckle_obtain", id -> Advancement.builder(id)
        .parent(ROOT.getId())
        .display(AdvancementDisplay.builder()
            .title(Component.text("Void Walker"))
            .description(Component.text("Plunder a Chorus Buckle from the heights of an End City."))
            .frame(AdvancementFrame.CHALLENGE)
            .icon(Items.CHORUS_BUCKLE.getStack())
            .build()
        )
        .criterion("has_relic", new ItemHasCriterion(ItemPredicate.builder().id(Items.CHORUS_BUCKLE.getId()).build()))
        .reward(new ExperienceReward(150))
        .build()
    );
    public static final Advancement CHORUS_BUCKLE_USE = ADVANCEMENTS.register("chorus_buckle_use", id -> Advancement.builder(id)
        .parent(ROOT.getId())
        .display(AdvancementDisplay.builder()
            .title(Component.text("Calculated Blink"))
            .description(Component.text("Perform a safe spatial jump using the Chorus Buckle."))
            .frame(AdvancementFrame.TASK)
            .icon(Items.CHORUS_BUCKLE.getStack())
            .build()
        )
        .build()
    );
}
