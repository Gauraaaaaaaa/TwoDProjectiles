package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDFireworkRocketRenderState;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.FireworkEntityRenderer;
import net.minecraft.client.renderer.entity.state.FireworkRocketRenderState;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkEntityRenderer.class)
public class FireworkEntityRendererMixin {

    @Inject(
            method = "render(Lnet/minecraft/client/renderer/entity/state/FireworkRocketRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",
                    shift = At.Shift.AFTER
            )
    )
    private void renderTwoDFireworkRocket(FireworkRocketRenderState fireworkRocketRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {

        if (TwoDProjectiles.CONFIG.renderTwoDFireworkRocket && fireworkRocketRenderState instanceof TwoDFireworkRocketRenderState twoDFireworkRocketRenderState) {

            poseStack.scale(TwoDProjectiles.CONFIG.fireworkRocketScale, TwoDProjectiles.CONFIG.fireworkRocketScale, TwoDProjectiles.CONFIG.fireworkRocketScale);

            if (fireworkRocketRenderState.isShotAtAngle) {

                poseStack.mulPose(Axis.YP.rotationDegrees(twoDFireworkRocketRenderState.twoDProjectiles$getYRot() - 90.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(twoDFireworkRocketRenderState.twoDProjectiles$getXRot() + TwoDProjectiles.CONFIG.fireworkRocketDirection.getDegree()));

                Quaternionf qY = Axis.YP.rotationDegrees(-90.0F);
                Quaternionf qZ = Axis.ZP.rotationDegrees(TwoDProjectiles.CONFIG.fireworkRocketDirection.getDegree() + (fireworkRocketRenderState.isShotAtAngle ? 0.0F : 90.0F));
                Quaternionf qCombined = new Quaternionf(qY).mul(qZ);

                Vector3f axis = new Vector3f();
                qCombined.normalizedPositiveZ(axis);

                poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(-90.0F), axis));

                poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(twoDFireworkRocketRenderState.twoDProjectiles$getRoll()), axis));
            }
            else {

                poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(TwoDProjectiles.CONFIG.fireworkRocketDirection.getDegree() + 90.0F));

                Quaternionf qY = Axis.YP.rotationDegrees(-90.0F);
                Quaternionf qZ = Axis.ZP.rotationDegrees(TwoDProjectiles.CONFIG.fireworkRocketDirection.getDegree() + 90.0F);
                Quaternionf qCombined = new Quaternionf(qY).mul(qZ);

                Vector3f axis = new Vector3f();
                qCombined.normalizedPositiveY(axis);

                poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(twoDFireworkRocketRenderState.twoDProjectiles$getRoll()), axis));
            }

            float offset = TwoDProjectiles.CONFIG.fireworkRocketOffset;
            float radiansZ = (float) Math.toRadians(TwoDProjectiles.CONFIG.fireworkRocketDirection.getDegree());
            float offsetX = -(float) Math.cos(radiansZ) * offset;
            float offsetY = (float) Math.sin(radiansZ) * offset;

            poseStack.translate(offsetX, offsetY - 0.125F, 0.0F);
        }
    }

    @Redirect(
            method = "render(Lnet/minecraft/client/renderer/entity/state/FireworkRocketRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V",
                    ordinal = 0
            )
    )
    private void cancelMulPose(PoseStack poseStack, Quaternionfc quaternionfc) {

        if (!TwoDProjectiles.CONFIG.renderTwoDFireworkRocket) {

            poseStack.mulPose(quaternionfc);
        }
    }

    @Redirect(
            method = "render(Lnet/minecraft/client/renderer/entity/state/FireworkRocketRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V",
                    ordinal = 1
            )
    )
    private void cancelRotationDegreesZP(PoseStack poseStack, Quaternionfc quaternionfc) {

        if (!TwoDProjectiles.CONFIG.renderTwoDFireworkRocket) {

            poseStack.mulPose(quaternionfc);
        }
    }

    @Redirect(
            method = "render(Lnet/minecraft/client/renderer/entity/state/FireworkRocketRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V",
                    ordinal = 2
            )
    )
    private void cancelRotationDegreesYP(PoseStack poseStack, Quaternionfc quaternionfc) {

        if (!TwoDProjectiles.CONFIG.renderTwoDFireworkRocket) {

            poseStack.mulPose(quaternionfc);
        }
    }

    @Redirect(
            method = "render(Lnet/minecraft/client/renderer/entity/state/FireworkRocketRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V",
                    ordinal = 3
            )
    )
    private void cancelRotationDegreesXP(PoseStack poseStack, Quaternionfc quaternionfc) {

        if (!TwoDProjectiles.CONFIG.renderTwoDFireworkRocket) {

            poseStack.mulPose(quaternionfc);
        }
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/projectile/FireworkRocketEntity;Lnet/minecraft/client/renderer/entity/state/FireworkRocketRenderState;F)V",
            at = @At("TAIL")
    )
    private void updateRenderState(FireworkRocketEntity fireworkRocketEntity, FireworkRocketRenderState fireworkRocketRenderState, float f, CallbackInfo ci) {

        if (TwoDProjectiles.CONFIG.renderTwoDFireworkRocket && fireworkRocketRenderState instanceof TwoDFireworkRocketRenderState twoDFireworkRocketRenderState) {

            twoDFireworkRocketRenderState.twoDProjectiles$setYRot(fireworkRocketEntity.getYRot(f));
            twoDFireworkRocketRenderState.twoDProjectiles$setXRot(fireworkRocketEntity.getXRot(f));
            twoDFireworkRocketRenderState.twoDProjectiles$setRoll(((TwoDRollEntity) fireworkRocketEntity).twod_projectiles$getRoll(f));
        }
    }
}
