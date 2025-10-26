package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import com.gaura.twod_projectiles.util.TwoDArrowRenderState;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(ArrowRenderer.class)
public class ArrowRendererMixin {

    @Unique
    private ItemModelResolver twod_projectiles$itemModelResolver;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererProvider.Context context, CallbackInfo ci) {

        this.twod_projectiles$itemModelResolver = context.getItemModelResolver();
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",
                    shift = At.Shift.AFTER
            )
    )
    private void updateScale(ArrowRenderState arrowRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {

        if (TwoDProjectiles.CONFIG.renderTwoDArrow) {

            poseStack.scale(TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale);
        }
    }

    @WrapOperation(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;",
                    ordinal = 1
            )
    )
    private Quaternionf modifyZPRotationDegrees(Axis axis, float f, Operation<Quaternionf> original, @Local(argsOnly = true) ArrowRenderState arrowRenderState) {

        float shake = 0.0F;

        if (arrowRenderState.shake > 0.0F) {

            shake = (-Mth.sin(arrowRenderState.shake * TwoDProjectiles.CONFIG.arrowShakeSpeedFactor) * arrowRenderState.shake * TwoDProjectiles.CONFIG.arrowShakePowerFactor) * ((float) Math.PI / 180F);
        }

        if (TwoDProjectiles.CONFIG.renderTwoDArrow && arrowRenderState instanceof TwoDArrowRenderState twoDArrowRenderState) {

            return original.call(axis, f + shake + twoDArrowRenderState.twod_projectiles$getArrowAngle());
        }
        else {

            return original.call(axis, f + shake);
        }
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void renderTwoDArrow(ArrowRenderState arrowRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {

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

//                twoDArrowRenderState.twoDProjectiles$getStack().render(poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY);
                twoDArrowRenderState.twoDProjectiles$getStack().submit(poseStack, submitNodeCollector, arrowRenderState.lightCoords, OverlayTexture.NO_OVERLAY, arrowRenderState.outlineColor);
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

//    @WrapOperation(
//            method = "render(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/model/ArrowModel;setupAnim(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;)V"
//            )
//    )
//    private void cancelSetupAnim(ArrowModel instance, ArrowRenderState f, Operation<Void> original) {}

    @WrapOperation(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"
            )
    )
    private void cancelRenderToBuffer(SubmitNodeCollector submitNodeCollector, Model<ArrowRenderState> model, Object object, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayTexture, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> original) {

        if (!TwoDProjectiles.CONFIG.renderTwoDArrow) {

            original.call(submitNodeCollector, model, object, poseStack, renderType, lightCoords, overlayTexture, outlineColor, crumblingOverlay);
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

            this.twod_projectiles$itemModelResolver.updateForNonLiving(twoDArrowRenderState.twoDProjectiles$getStack(), itemStack, ItemDisplayContext.GROUND, abstractArrow);
        }
    }
}
