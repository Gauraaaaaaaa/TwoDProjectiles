package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin implements TwoDRollEntity {

    @Shadow
    protected abstract boolean isInGround();

    @Unique
    private float twod_projectiles$roll = 0.0F;

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;isCritArrow()Z"
            )
    )
    private boolean removeCriticalParticles(AbstractArrow abstractArrow) {

        return abstractArrow.isCritArrow() && TwoDProjectiles.CONFIG.render_critical_particles;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {

        AbstractArrow abstractArrow = (AbstractArrow) (Object) this;

        if (!this.isInGround()) {

            twod_projectiles$roll += (float) (TwoDProjectiles.CONFIG.arrow_roll * abstractArrow.getDeltaMovement().length());
        }
    }

    @Override
    public float twod_projectiles$getRoll(float f) {

        if (this.isInGround()) return twod_projectiles$roll % 360.0F;

        double speed = ((AbstractArrow) (Object) this).getDeltaMovement().length();
        float interpolated = twod_projectiles$roll + (float) (TwoDProjectiles.CONFIG.arrow_roll * speed * f);
        return interpolated % 360.0F;
    }
}
