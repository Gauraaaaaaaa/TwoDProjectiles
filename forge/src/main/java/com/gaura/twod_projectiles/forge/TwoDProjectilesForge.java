package com.gaura.twod_projectiles.forge;

import com.gaura.twod_projectiles.config.TwoDProjectilesConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.common.Mod;

import com.gaura.twod_projectiles.TwoDProjectiles;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TwoDProjectiles.MOD_ID)
public final class TwoDProjectilesForge {

    private final FMLJavaModLoadingContext fmlCtx;

    public TwoDProjectilesForge(FMLJavaModLoadingContext context) {

        this.fmlCtx = context;

        TwoDProjectiles.init();

        context.getModEventBus().addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {

        fmlCtx.getContainer().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> AutoConfig.getConfigScreen(TwoDProjectilesConfig.class, parent).get()
                )
        );
    }
}
