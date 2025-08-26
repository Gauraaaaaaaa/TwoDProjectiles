package com.gaura.twod_projectiles.fabric;

import net.fabricmc.api.ClientModInitializer;

import com.gaura.twod_projectiles.TwoDProjectiles;

public final class TwoDProjectilesFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        TwoDProjectiles.init();
    }
}
