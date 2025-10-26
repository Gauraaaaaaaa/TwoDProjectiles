package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDThrownItemRenderState;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownItemRenderer.class)
public class ThrownItemRendererMixin {

    @WrapOperation(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownItemRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"
            )
    )
    private void updateScale(PoseStack poseStack, float x, float y, float z, Operation<Void> original) {

        if (TwoDProjectiles.CONFIG.renderTwoDProjectileItem) {

            original.call(poseStack, TwoDProjectiles.CONFIG.projectileItemScale, TwoDProjectiles.CONFIG.projectileItemScale, TwoDProjectiles.CONFIG.projectileItemScale);
        }
        else {

            original.call(poseStack, x, y, z);
        }
    }

    @WrapOperation(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownItemRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V"
            )
    )
    private void cancelMulPose(PoseStack poseStack, Quaternionfc quaternionfc, Operation<Void> original, @Local(argsOnly = true) ThrownItemRenderState thrownItemRenderState) {

        if (TwoDProjectiles.CONFIG.renderTwoDProjectileItem && thrownItemRenderState instanceof TwoDThrownItemRenderState twoDThrownItemRenderState) {

            poseStack.mulPose(Axis.YP.rotationDegrees(twoDThrownItemRenderState.twod_projectiles$getYRot() - 180.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(twoDThrownItemRenderState.twod_projectiles$getXRot()));
        }
        else {

            original.call(poseStack, quaternionfc);
        }
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/ThrownItemRenderState;F)V",
            at = @At(value = "TAIL")
    )
    private void updateRenderState(Entity entity, ThrownItemRenderState thrownItemRenderState, float f, CallbackInfo ci) {

        if (TwoDProjectiles.CONFIG.renderTwoDProjectileItem && thrownItemRenderState instanceof TwoDThrownItemRenderState twoDThrownItemRenderState) {

            twoDThrownItemRenderState.twod_projectiles$setXRot(entity.getXRot(f));
            twoDThrownItemRenderState.twod_projectiles$setYRot(entity.getYRot(f));
        }
    }
}
