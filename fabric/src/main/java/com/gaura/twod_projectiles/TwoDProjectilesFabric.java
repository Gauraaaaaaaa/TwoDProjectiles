package com.gaura.twod_projectiles;

import net.fabricmc.api.ClientModInitializer;

public final class TwoDProjectilesFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        TwoDProjectiles.init();
    }
}
