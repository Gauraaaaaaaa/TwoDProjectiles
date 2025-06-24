package com.gaura.twod_projectiles.util;

import net.minecraft.client.renderer.item.ItemStackRenderState;

public interface TwoDArrowRenderState {

    boolean twoDProjectiles$isArrowFromCrossbow();

    ItemStackRenderState twoDProjectiles$getStack();

    void twoDProjectiles$setArrowFromCrossbow(boolean arrowFromCrossbow);

//    ArrayList<Vec3> twod_projectiles$getTrailPoints();
//
//    void twod_projectiles$setTrailPoints(ArrayList<Vec3> trailPoints);

    float twod_projectiles$getRoll();

    void twod_projectiles$setRoll(float roll);

    float twod_projectiles$getArrowAngle();

    void twod_projectiles$setArrowAngle(float arrowAngle);
}
