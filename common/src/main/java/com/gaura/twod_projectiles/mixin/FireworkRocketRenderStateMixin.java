package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.util.TwoDFireworkRocketRenderState;
import net.minecraft.client.renderer.entity.state.FireworkRocketRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FireworkRocketRenderState.class)
public class FireworkRocketRenderStateMixin implements TwoDFireworkRocketRenderState {

    @Unique
    private float twod_projectiles$yRot;

    @Unique
    private float twod_projectiles$xRot;

    @Unique
    private float twod_projectiles$roll;

    @Override
    public float twoDProjectiles$getYRot() {

        return twod_projectiles$yRot;
    }

    @Override
    public float twoDProjectiles$getXRot() {

        return twod_projectiles$xRot;
    }

    @Override
    public void twoDProjectiles$setYRot(float yRot) {

        this.twod_projectiles$yRot = yRot;
    }

    @Override
    public void twoDProjectiles$setXRot(float xRot) {

        this.twod_projectiles$xRot = xRot;
    }

    @Override
    public float twoDProjectiles$getRoll() {

        return twod_projectiles$roll;
    }

    @Override
    public void twoDProjectiles$setRoll(float roll) {

        this.twod_projectiles$roll = roll;
    }
}
