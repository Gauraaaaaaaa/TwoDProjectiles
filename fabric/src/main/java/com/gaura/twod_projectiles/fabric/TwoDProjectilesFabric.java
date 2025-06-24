package com.gaura.twod_projectiles.fabric;

import net.fabricmc.api.ModInitializer;

import com.gaura.twod_projectiles.TwoDProjectiles;

public final class TwoDProjectilesFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        TwoDProjectiles.init();
    }
}
