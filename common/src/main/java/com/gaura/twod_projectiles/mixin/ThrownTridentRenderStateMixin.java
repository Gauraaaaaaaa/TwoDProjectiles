package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.util.TwoDThrownTridentRenderState;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTridentRenderState.class)
public class ThrownTridentRenderStateMixin implements TwoDThrownTridentRenderState {

    @Unique
    private ItemStack twod_projectiles$itemStack;

    @Unique
    @Nullable
    private BakedModel twod_projectiles$bakedModel;

    @Unique
    private float twod_projectiles$shake;

    @Unique
    private float twod_projectiles$roll;

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
    public float twod_projectiles$getShake() {

        return twod_projectiles$shake;
    }

    @Override
    public void twod_projectiles$setShake(float shake) {

        this.twod_projectiles$shake = shake;
    }

    @Override
    public float twod_projectiles$getRoll() {

        return twod_projectiles$roll;
    }

    @Override
    public void twod_projectiles$setRoll(float roll) {

        this.twod_projectiles$roll = roll;
    }

    @Inject(method ="<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {

        this.twod_projectiles$itemStack = ItemStack.EMPTY;
    }
}
