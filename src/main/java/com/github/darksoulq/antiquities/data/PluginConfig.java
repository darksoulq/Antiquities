package com.github.darksoulq.antiquities.data;

import com.github.darksoulq.abyssallib.common.config.Config;
import com.github.darksoulq.antiquities.Antiquities;

public final class PluginConfig {
    public final Config cfg;
    public final Config.Value<Boolean> metrics;

    public PluginConfig(Antiquities plugin) {
        cfg = new Config(Antiquities.PLUGIN_ID, "config");

        metrics = cfg.value("metrics", true);
    }
}
