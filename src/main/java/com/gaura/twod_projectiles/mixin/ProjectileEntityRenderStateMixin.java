package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.util.TwoDProjectileEntityRenderState;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ProjectileEntityRenderState.class)
public class ProjectileEntityRenderStateMixin implements TwoDProjectileEntityRenderState {

    @Unique
    private boolean arrowFromCrossbow;

    @Unique
    @Nullable
    private BakedModel model;

    @Unique
    private ItemStack stack;

    public ProjectileEntityRenderStateMixin() {

        this.stack = ItemStack.EMPTY;
    }

    @Override
    public boolean twoDProjectiles$isArrowFromCrossbow() {

        return arrowFromCrossbow;
    }

    @Override
    public @Nullable BakedModel twoDProjectiles$getModel() {

        return model;
    }

    @Override
    public ItemStack twoDProjectiles$getStack() {

        return stack;
    }

    @Override
    public void twoDProjectiles$setArrowFromCrossbow(boolean arrowFromCrossbow) {

        this.arrowFromCrossbow = arrowFromCrossbow;
    }

    @Override
    public void twoDProjectiles$setModel(BakedModel model) {

        this.model = model;
    }

    @Override
    public void twoDProjectiles$setStack(ItemStack stack) {

        this.stack = stack;
    }
}