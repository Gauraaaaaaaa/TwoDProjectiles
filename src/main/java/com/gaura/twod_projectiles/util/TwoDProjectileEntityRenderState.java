package com.gaura.twod_projectiles.util;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;

public interface TwoDProjectileEntityRenderState {

    boolean twoDProjectiles$isArrowFromCrossbow();

    BakedModel twoDProjectiles$getModel();

    ItemStack twoDProjectiles$getStack();

    void twoDProjectiles$setArrowFromCrossbow(boolean arrowFromCrossbow);

    void twoDProjectiles$setModel(BakedModel model);

    void twoDProjectiles$setStack(ItemStack stack);
}
