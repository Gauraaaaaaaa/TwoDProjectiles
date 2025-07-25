package com.gaura.twod_projectiles.util;

import net.minecraft.client.renderer.item.ItemStackRenderState;

public interface TwoDThrownTridentRenderState {

    ItemStackRenderState twoDProjectiles$getStack();

    float twod_projectiles$getShake();

    void twod_projectiles$setShake(float shake);

    float twod_projectiles$getRoll();

    void twod_projectiles$setRoll(float roll);

    float twod_projectiles$getTridentAngle();

    void twod_projectiles$setTridentAngle(float tridentAngle);
}
