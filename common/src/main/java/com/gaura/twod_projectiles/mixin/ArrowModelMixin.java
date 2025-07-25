package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDArrowRenderState;
import net.minecraft.client.model.ArrowModel;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowModel.class)
public class ArrowModelMixin {

//    @ModifyVariable(
//            method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;)V",
//            at = @At(
//                    value = "STORE",
//                    ordinal = 0
//            ),
//            name = "f"
//    )
//    private float modifyShake(float f, ArrowRenderState arrowRenderState) {
//
//        return -Mth.sin(arrowRenderState.shake * TwoDProjectiles.CONFIG.arrowShakePowerFactor) * arrowRenderState.shake * TwoDProjectiles.CONFIG.arrowShakeSpeedFactor;
//    }

//    @Inject(
//            method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;)V",
//            at = @At("TAIL")
//    )
//    private void setupAnim(ArrowRenderState arrowRenderState, CallbackInfo ci) {
//
//        if (!TwoDProjectiles.CONFIG.renderTwoDArrow && arrowRenderState instanceof TwoDArrowRenderState twoDArrowRenderState) {
//
//            ArrowModel arrowModel = (ArrowModel) (Object) this;
//
//            arrowModel.root().xRot += twoDArrowRenderState.twod_projectiles$getRoll();
//        }
//    }
}
