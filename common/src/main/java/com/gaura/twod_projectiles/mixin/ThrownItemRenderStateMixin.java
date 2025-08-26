package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.util.TwoDThrownItemRenderState;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ThrownItemRenderState.class)
public class ThrownItemRenderStateMixin implements TwoDThrownItemRenderState {

    @Unique
    private float twod_projectiles$xRot;

    @Unique
    private float twod_projectiles$yRot;

    @Override
    public float twod_projectiles$getXRot() {

        return twod_projectiles$xRot;
    }

    @Override
    public void twod_projectiles$setXRot(float xRot) {

        this.twod_projectiles$xRot = xRot;
    }

    @Override
    public float twod_projectiles$getYRot() {

        return twod_projectiles$yRot;
    }

    @Override
    public void twod_projectiles$setYRot(float yRot) {

        this.twod_projectiles$yRot = yRot;
    }
}
