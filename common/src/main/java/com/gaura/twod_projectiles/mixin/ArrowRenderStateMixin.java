package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.util.TwoDArrowRenderState;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowRenderState.class)
public class ArrowRenderStateMixin implements TwoDArrowRenderState {

    @Unique
    private ItemStack twod_projectiles$itemStack;

    @Unique
    @Nullable
    private BakedModel twod_projectiles$bakedModel;

    @Unique
    private float twod_projectiles$roll;

    @Unique
    private float twod_projectiles$arrowAngle;

    @Override
    public ItemStack twoDProjectiles$getItemStack() {

        return twod_projectiles$itemStack;
    }

    @Override
    public void twoDProjectiles$setItemStack(ItemStack itemStack) {

        this.twod_projectiles$itemStack = itemStack;
    }

    @Override
    public BakedModel twod_projectiles$getBakedModel() {

        return twod_projectiles$bakedModel;
    }

    @Override
    public void twod_projectiles$setBakedModel(BakedModel bakedModel) {

        this.twod_projectiles$bakedModel = bakedModel;
    }

    @Override
    public float twod_projectiles$getRoll() {

        return twod_projectiles$roll;
    }

    @Override
    public void twod_projectiles$setRoll(float roll) {

        this.twod_projectiles$roll = roll;
    }

    @Override
    public float twod_projectiles$getArrowAngle() {

        return twod_projectiles$arrowAngle;
    }

    @Override
    public void twod_projectiles$setArrowAngle(float arrowAngle) {

        this.twod_projectiles$arrowAngle = arrowAngle;
    }

    @Inject(method ="<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {

        this.twod_projectiles$itemStack = ItemStack.EMPTY;
    }
}
