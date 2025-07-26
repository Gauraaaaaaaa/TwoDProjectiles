package com.gaura.twod_projectiles.util;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

public interface TwoDArrowRenderState {

    ItemStack twoDProjectiles$getItemStack();

    void twoDProjectiles$setItemStack(ItemStack itemStack);

    BakedModel twod_projectiles$getBakedModel();

    void twod_projectiles$setBakedModel(BakedModel bakedModel);

    float twod_projectiles$getRoll();

    void twod_projectiles$setRoll(float roll);

    float twod_projectiles$getArrowAngle();

    void twod_projectiles$setArrowAngle(float arrowAngle);
}
