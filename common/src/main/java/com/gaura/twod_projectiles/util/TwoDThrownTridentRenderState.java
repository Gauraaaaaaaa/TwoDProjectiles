package com.gaura.twod_projectiles.util;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

public interface TwoDThrownTridentRenderState {

    ItemStack twoDProjectiles$getItemStack();

    void twoDProjectiles$setItemStack(ItemStack itemStack);

    BakedModel twod_projectiles$getBakedModel();

    void twod_projectiles$setBakedModel(BakedModel bakedModel);

    float twod_projectiles$getShake();

    void twod_projectiles$setShake(float shake);

    float twod_projectiles$getRoll();

    void twod_projectiles$setRoll(float roll);

    float twod_projectiles$getTridentAngle();

    void twod_projectiles$setTridentAngle(float tridentAngle);
}
