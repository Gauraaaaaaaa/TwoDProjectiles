package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin implements TwoDRollEntity {

    @Unique
    private float twod_projectiles$roll = 0.0F;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {

        twod_projectiles$roll += TwoDProjectiles.CONFIG.fireworkRocketRoll;
    }

    @Override
    public float twod_projectiles$getRoll(float f) {

        return (twod_projectiles$roll + f * TwoDProjectiles.CONFIG.fireworkRocketRoll) % 360.0F;
    }
}
