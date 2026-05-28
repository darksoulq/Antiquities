package com.github.darksoulq.antiquities.data;

import com.github.darksoulq.abyssallib.server.resource.Namespace;
import com.github.darksoulq.abyssallib.server.resource.ResourcePack;
import com.github.darksoulq.abyssallib.server.resource.asset.Lang;
import com.github.darksoulq.abyssallib.server.resource.asset.Model;
import com.github.darksoulq.abyssallib.server.resource.asset.definition.Selector;
import com.github.darksoulq.abyssallib.server.resource.util.ItemModels;
import com.github.darksoulq.antiquities.Antiquities;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class Pack {
    public Pack(Antiquities plugin) {
        ResourcePack pack = new ResourcePack(plugin, Antiquities.PLUGIN_ID);
        Namespace ns = pack.namespace(Antiquities.PLUGIN_ID);
        ns.icon();

        createItemModels(ns);
        createDefaultLanguage(ns);

        pack.register(false);
    }

    private void createDefaultLanguage(Namespace ns) {
        Lang ln = ns.lang("en_us", false);

        String[] items = {
            "piglin_signet",
            "echoing_circlet",
            "idol_of_depths",
            "evokers_mantle",
            "strider_hide_girdle",
            "membrane_cowl",
            "chorus_buckle",
            "blaze_ember_ring",
            "witchs_brewstone"
        };

        for (String id : items) {
            ln.put("item.antiquities." + id, getDisplayName(id));
        }

        ln.put("category.item.antiquities.all", "All Items");
        ln.put("plugin.antiquities", "Antiquities");
    }
    private void createItemModels(Namespace ns) {
        Model piglinSignet = ItemModels.generated(ns, "piglin_signet", ns.texture("item/piglin_signet"));
        createDefinition(ns, piglinSignet, "piglin_signet");

        Model echoingCirclet = ItemModels.generated(ns, "echoing_circlet", ns.texture("item/echoing_circlet"));
        createDefinition(ns, echoingCirclet, "echoing_circlet");

        Model idolOfDepths = ItemModels.generated(ns, "idol_of_depths", ns.texture("item/idol_of_depths"));
        createDefinition(ns, idolOfDepths, "idol_of_depths");

        Model evokersMantle = ItemModels.generated(ns, "evokers_mantle", ns.texture("item/evokers_mantle"));
        createDefinition(ns, evokersMantle, "evokers_mantle");

        Model striderHideGirdle = ItemModels.generated(ns, "strider_hide_girdle", ns.texture("item/strider_hide_girdle"));
        createDefinition(ns, striderHideGirdle, "strider_hide_girdle");

        Model membraneCowl = ItemModels.generated(ns, "membrane_cowl", ns.texture("item/membrane_cowl"));
        createDefinition(ns, membraneCowl, "membrane_cowl");

        Model chorusBuckle = ItemModels.generated(ns, "chorus_buckle", ns.texture("item/chorus_buckle"));
        createDefinition(ns, chorusBuckle, "chorus_buckle");

        Model blazeEmberRing = ItemModels.generated(ns, "blaze_ember_ring", ns.texture("item/blaze_ember_ring"));
        createDefinition(ns, blazeEmberRing, "blaze_ember_ring");

        Model witchsBrewstone = ItemModels.generated(ns, "witchs_brewstone", ns.texture("item/witchs_brewstone"));
        createDefinition(ns, witchsBrewstone, "witchs_brewstone");
    }

    private void createDefinition(Namespace ns, Model model, String path) {
        Selector.Model selector = new Selector.Model(model);
        ns.itemDefinition(path, selector);
    }
    private String getDisplayName(String id) {
        return switch (id) {
            case "evokers_mantle" -> "Evoker's Mantle";
            case "witchs_brewstone" -> "Witch's Brewstone";
            default -> Arrays.stream(id.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
        };
    }
}
