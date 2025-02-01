package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.util.TwoDFireworkRocketEntityRenderState;
import net.minecraft.client.render.entity.state.FireworkRocketEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FireworkRocketEntityRenderState.class)
public class FireworkRocketEntityRenderStateMixin implements TwoDFireworkRocketEntityRenderState {

    @Unique
    private float yaw;

    @Unique
    private float pitch;

    @Override
    public float twoDProjectiles$getYaw() {

        return yaw;
    }

    @Override
    public float twoDProjectiles$getPitch() {

        return pitch;
    }

    @Override
    public void twoDProjectiles$setYaw(float yaw) {

        this.yaw = yaw;
    }

    @Override
    public void twoDProjectiles$setPitch(float pitch) {

        this.pitch = pitch;
    }
}
