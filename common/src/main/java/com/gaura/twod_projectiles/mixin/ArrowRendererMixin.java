package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import com.gaura.twod_projectiles.util.TwoDArrowRenderState;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArrowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(ArrowRenderer.class)
public class ArrowRendererMixin {

    @Unique
    private ItemRenderer twod_projectiles$itemRenderer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererProvider.Context context, CallbackInfo ci) {

        this.twod_projectiles$itemRenderer = context.getItemRenderer();
    }

    @Inject(
            method = "render(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",
                    shift = At.Shift.AFTER
            )
    )
    private void updateScale(ArrowRenderState arrowRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {

        if (TwoDProjectiles.CONFIG.renderTwoDArrow) {

            poseStack.scale(TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale);
        }
    }

    @Redirect(
            method = "render(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 1
            )
    )
    private void modifyZPRotationDegrees(PoseStack poseStack, Quaternionf quaternionf, @Local(argsOnly = true) ArrowRenderState arrowRenderState) {

        float shake = 0.0F;

        if (arrowRenderState.shake > 0.0F) {

            shake = (-Mth.sin(arrowRenderState.shake * TwoDProjectiles.CONFIG.arrowShakeSpeedFactor) * arrowRenderState.shake * TwoDProjectiles.CONFIG.arrowShakePowerFactor) * ((float) Math.PI / 180F);
        }

        if (TwoDProjectiles.CONFIG.renderTwoDArrow && arrowRenderState instanceof TwoDArrowRenderState twoDArrowRenderState) {

            poseStack.mulPose(Axis.ZP.rotationDegrees(arrowRenderState.xRot + shake + twoDArrowRenderState.twod_projectiles$getArrowAngle()));
        }
        else {

            poseStack.mulPose(Axis.ZP.rotationDegrees(arrowRenderState.xRot + shake));
        }
    }

    @Inject(
            method = "render(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void renderTwoDArrow(ArrowRenderState arrowRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {

        if (arrowRenderState instanceof TwoDArrowRenderState twoDArrowRenderState) {

            if (TwoDProjectiles.CONFIG.renderTwoDArrow) {

                Quaternionf qY = Axis.YP.rotationDegrees(-90.0F);
                Quaternionf qZ = Axis.ZP.rotationDegrees(twoDArrowRenderState.twod_projectiles$getArrowAngle());
                Quaternionf qCombined = new Quaternionf(qY).mul(qZ);

                Vector3f axis = new Vector3f();
                qCombined.normalizedPositiveZ(axis);

                poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(twoDArrowRenderState.twod_projectiles$getRoll()), axis));

                float offset = TwoDProjectiles.CONFIG.arrowOffset;
                float radiansZ = (float) Math.toRadians(twoDArrowRenderState.twod_projectiles$getArrowAngle());
                float offsetX = -(float) Math.cos(radiansZ) * offset;
                float offsetY = (float) Math.sin(radiansZ) * offset;

                poseStack.translate(offsetX, offsetY - 0.125F, 0.0F);

                this.twod_projectiles$itemRenderer.render(twoDArrowRenderState.twoDProjectiles$getItemStack(), ItemDisplayContext.GROUND, false, poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY, twoDArrowRenderState.twod_projectiles$getBakedModel());
            }
            else {

                Quaternionf qY = Axis.YP.rotationDegrees(-90.0F);
                Quaternionf qZ = Axis.ZP.rotationDegrees(0.0F);
                Quaternionf qCombined = new Quaternionf(qY).mul(qZ);

                Vector3f axis = new Vector3f();
                qCombined.normalizedPositiveZ(axis);

                poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(twoDArrowRenderState.twod_projectiles$getRoll()), axis));
            }
        }
    }

    @Redirect(
            method = "render(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ArrowModel;setupAnim(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;)V"
            )
    )
    private void cancelSetupAnim(ArrowModel arrowModel, ArrowRenderState arrowRenderState) {}

    @Redirect(
            method = "render(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ArrowModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"
            )
    )
    private void cancelRenderToBuffer(ArrowModel arrowModel, PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j) {

        if (!TwoDProjectiles.CONFIG.renderTwoDArrow) {

            arrowModel.renderToBuffer(poseStack, vertexConsumer, i, j);
        }
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/projectile/AbstractArrow;Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;F)V",
            at = @At("TAIL")
    )
    private void updateRenderState(AbstractArrow abstractArrow, ArrowRenderState arrowRenderState, float f, CallbackInfo ci) {

        if (arrowRenderState instanceof TwoDArrowRenderState twoDArrowRenderState) {

            twoDArrowRenderState.twod_projectiles$setRoll(((TwoDRollEntity) abstractArrow).twod_projectiles$getRoll(f));

            ItemStack itemStack = ((AbstractArrowInvoker) abstractArrow).invokeGetPickupItem();

            if (TwoDProjectiles.CONFIG.renderTippedArrow && abstractArrow instanceof Arrow arrow && arrow.getColor() != -1) {

                itemStack = Items.TIPPED_ARROW.getDefaultInstance();
                itemStack.set(DataComponents.POTION_CONTENTS, arrow.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(arrow.getColor()), List.of(), Optional.empty())));
            }

            twoDArrowRenderState.twod_projectiles$setArrowAngle(TwoDProjectiles.getArrowAngle(itemStack));

            twoDArrowRenderState.twoDProjectiles$setItemStack(itemStack.copy());
            twoDArrowRenderState.twod_projectiles$setBakedModel(!itemStack.isEmpty() ? this.twod_projectiles$itemRenderer.getModel(itemStack, abstractArrow.level(), null, abstractArrow.getId()) : null);
        }
    }
}
