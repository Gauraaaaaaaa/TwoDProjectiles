package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin implements TwoDRollEntity {

    @Unique
    private float twod_projectiles$roll = 0.0F;

    @Unique
    private final int twod_projectiles$random = RandomSource.create().nextBoolean() ? 1 : -1;

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;isCritArrow()Z"
            )
    )
    private boolean removeCriticalParticles(AbstractArrow abstractArrow, Operation<Boolean> original) {

        return original.call(abstractArrow) && TwoDProjectiles.CONFIG.renderCriticalParticles;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {

        AbstractArrow abstractArrow = (AbstractArrow) (Object) this;

        if (!((AbstractArrowInvoker) abstractArrow).invokeIsInGround()) {

            this.twod_projectiles$roll += (float) (this.twod_projectiles$random * TwoDProjectiles.CONFIG.arrowRoll * abstractArrow.getDeltaMovement().length());
        }
    }

    @Override
    public float twod_projectiles$getRoll(float f) {

        AbstractArrow abstractArrow = (AbstractArrow) (Object) this;

        if (((AbstractArrowInvoker) abstractArrow).invokeIsInGround()) {

            return this.twod_projectiles$roll % 360.0F;
        }
        else {

            float speed = (float) abstractArrow.getDeltaMovement().length();
            float interpolated = this.twod_projectiles$roll + (this.twod_projectiles$random * TwoDProjectiles.CONFIG.arrowRoll * speed * f);
            return interpolated % 360.0F;
        }
    }
}
