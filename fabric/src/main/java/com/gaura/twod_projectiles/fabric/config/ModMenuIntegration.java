package com.gaura.twod_projectiles.fabric.config;

import com.gaura.twod_projectiles.config.TwoDProjectilesConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfigClient;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {

        return parent -> AutoConfigClient.getConfigScreen(TwoDProjectilesConfig.class, parent).get();
    }
}
