package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin implements TwoDRollEntity {

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

        return abstractArrow.isCritArrow() && TwoDProjectiles.CONFIG.renderCriticalParticles;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {

        AbstractArrow abstractArrow = (AbstractArrow) (Object) this;

        if (!((AbstractArrowInvoker) abstractArrow).invokeIsInGround()) {

            twod_projectiles$roll += (float) (TwoDProjectiles.CONFIG.arrowRoll * abstractArrow.getDeltaMovement().length());
        }
    }

    @Override
    public float twod_projectiles$getRoll(float f) {

        AbstractArrow abstractArrow = (AbstractArrow) (Object) this;

        if (((AbstractArrowInvoker) abstractArrow).invokeIsInGround()) {

            return twod_projectiles$roll % 360.0F;
        }
        else {

            float speed = (float) abstractArrow.getDeltaMovement().length();
            float interpolated = twod_projectiles$roll + (TwoDProjectiles.CONFIG.arrowRoll * speed * f);
            return interpolated % 360.0F;
        }
    }
}
