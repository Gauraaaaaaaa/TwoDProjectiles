package com.gaura.twod_projectiles.util;

import net.minecraft.client.render.item.ItemRenderState;

public interface TwoDProjectileEntityRenderState {

    boolean twoDProjectiles$isArrowFromCrossbow();

    ItemRenderState twoDProjectiles$getStack();

    void twoDProjectiles$setArrowFromCrossbow(boolean arrowFromCrossbow);
}
