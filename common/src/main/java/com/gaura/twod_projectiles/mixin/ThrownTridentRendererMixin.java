package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTridentRenderer.class)
public class ThrownTridentRendererMixin {

    @Unique
    private ItemRenderer twod_projectiles$itemRenderer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererProvider.Context context, CallbackInfo ci) {

        this.twod_projectiles$itemRenderer = context.getItemRenderer();
    }

    @Inject(
            method = "render(Lnet/minecraft/world/entity/projectile/ThrownTrident;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",
                    shift = At.Shift.AFTER
            )
    )
    private void updateScale(ThrownTrident thrownTrident, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {

        if (TwoDProjectiles.CONFIG.renderTwoDTrident) {

            poseStack.scale(TwoDProjectiles.CONFIG.tridentScale, TwoDProjectiles.CONFIG.tridentScale, TwoDProjectiles.CONFIG.tridentScale);
        }
    }

    @ModifyConstant(
            method = "render(Lnet/minecraft/world/entity/projectile/ThrownTrident;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            constant = @Constant(
                    floatValue = 90.0F,
                    ordinal = 1
            )
    )
    private float modifyZPRotationDegrees(float f) {

        if (TwoDProjectiles.CONFIG.renderTwoDTrident) {

            return TwoDProjectiles.CONFIG.tridentDirection.getDegree();
        }
        else {

            return f;
        }
    }

    @Inject(
            method = "render(Lnet/minecraft/world/entity/projectile/ThrownTrident;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void renderTwoDTrident(ThrownTrident thrownTrident, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {

        float s = (float) thrownTrident.shakeTime - g;

        if (s > 0.0F) {

            float t = (-Mth.sin(s * TwoDProjectiles.CONFIG.tridentShakeSpeedFactor) * s * TwoDProjectiles.CONFIG.tridentShakePowerFactor) * ((float) Math.PI / 180F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(t));
        }

        if (TwoDProjectiles.CONFIG.renderTwoDTrident) {

            Quaternionf qY = Axis.YP.rotationDegrees(-90.0F);
            Quaternionf qZ = Axis.ZP.rotationDegrees(TwoDProjectiles.CONFIG.tridentDirection.getDegree());
            Quaternionf qCombined = new Quaternionf(qY).mul(qZ);

            Vector3f axis = new Vector3f();
            qCombined.normalizedPositiveZ(axis);

            poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(((TwoDRollEntity) thrownTrident).twod_projectiles$getRoll(g)), axis));

            float offset = TwoDProjectiles.CONFIG.tridentOffset;
            float radiansZ = (float) Math.toRadians(TwoDProjectiles.CONFIG.tridentDirection.getDegree());
            float offsetX = -(float) Math.cos(radiansZ) * offset;
            float offsetY = (float) Math.sin(radiansZ) * offset;

            poseStack.translate(offsetX, offsetY - 0.125F, 0.0F);

            this.twod_projectiles$itemRenderer.renderStatic(thrownTrident.getWeaponItem(), ItemDisplayContext.GROUND, i, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, thrownTrident.level(), thrownTrident.getId());
        }
        else {

            Quaternionf qY = Axis.YP.rotationDegrees(-90.0F);
            Quaternionf qZ = Axis.ZP.rotationDegrees(90.0F);
            Quaternionf qCombined = new Quaternionf(qY).mul(qZ);

            Vector3f axis = new Vector3f();
            qCombined.normalizedPositiveZ(axis);

            poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(((TwoDRollEntity) thrownTrident).twod_projectiles$getRoll(g)), axis));
        }
    }

    @WrapOperation(
            method = "render(Lnet/minecraft/world/entity/projectile/ThrownTrident;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/TridentModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"
            )
    )
    private void cancelRenderToBuffer(TridentModel tridentModel, PoseStack poseStack, VertexConsumer vertexConsumer, int i, int overlayTexture, Operation<Void> original) {

        if (!TwoDProjectiles.CONFIG.renderTwoDTrident) {

            original.call(tridentModel, poseStack, vertexConsumer, i, overlayTexture);
        }
    }
}
