package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.ThrownTrident;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public class ThrownTridentMixin implements TwoDRollEntity {

    @Unique
    private float twod_projectiles$roll = 0.0F;

    @Unique
    private final int twod_projectiles$random = RandomSource.create().nextBoolean() ? 1 : -1;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {

        ThrownTrident thrownTrident = (ThrownTrident) (Object) this;

        if (!((AbstractArrowInvoker) thrownTrident).isInGround()) {

            this.twod_projectiles$roll += (float) (this.twod_projectiles$random * TwoDProjectiles.CONFIG.tridentRoll * thrownTrident.getDeltaMovement().length());
        }
    }

    @Override
    public float twod_projectiles$getRoll(float f) {

        ThrownTrident thrownTrident = (ThrownTrident) (Object) this;

        if (((AbstractArrowInvoker) thrownTrident).isInGround()) {

            return this.twod_projectiles$roll % 360.0F;
        }
        else {

            float speed = (float) thrownTrident.getDeltaMovement().length();
            float interpolated = this.twod_projectiles$roll + (this.twod_projectiles$random * TwoDProjectiles.CONFIG.tridentRoll * speed * f);
            return interpolated % 360.0F;
        }
    }
}
