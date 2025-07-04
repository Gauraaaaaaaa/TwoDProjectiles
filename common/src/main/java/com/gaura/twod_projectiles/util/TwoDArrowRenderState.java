package com.gaura.twod_projectiles.util;

import net.minecraft.client.renderer.item.ItemStackRenderState;

public interface TwoDArrowRenderState {

    ItemStackRenderState twoDProjectiles$getStack();

    float twod_projectiles$getRoll();

    void twod_projectiles$setRoll(float roll);

    float twod_projectiles$getArrowAngle();

    void twod_projectiles$setArrowAngle(float arrowAngle);
}
