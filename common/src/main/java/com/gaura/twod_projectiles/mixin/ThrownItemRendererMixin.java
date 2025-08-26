package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownItemRenderer.class)
public class ThrownItemRendererMixin {

    @WrapOperation(
            method = "render",
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
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V"
            )
    )
    private void cancelMulPose(PoseStack poseStack, Quaternionf quaternionf, Operation<Void> original) {

        if (!TwoDProjectiles.CONFIG.renderTwoDProjectileItem) {

            original.call(poseStack, quaternionf);
        }
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void renderTwoDThrownItem(Entity entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {

        if (TwoDProjectiles.CONFIG.renderTwoDProjectileItem) {

            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, entity.yRotO, entity.getYRot()) - 180.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(g, entity.xRotO, entity.getXRot())));
        }
    }
}
