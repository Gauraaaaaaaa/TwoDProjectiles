package com.gaura.twod_projectiles.neoforge;

import com.gaura.twod_projectiles.config.TwoDProjectilesConfig;
import me.shedaniel.autoconfig.AutoConfigClient;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;

import com.gaura.twod_projectiles.TwoDProjectiles;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(TwoDProjectiles.MOD_ID)
public final class TwoDProjectilesNeoForge {

    public TwoDProjectilesNeoForge() {

        TwoDProjectiles.init();

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> AutoConfigClient.getConfigScreen(TwoDProjectilesConfig.class, parent).get()
        );
    }
}
