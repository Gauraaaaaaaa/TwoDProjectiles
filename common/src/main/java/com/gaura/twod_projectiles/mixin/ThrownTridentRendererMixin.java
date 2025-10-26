package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import com.gaura.twod_projectiles.util.TwoDThrownTridentRenderState;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTridentRenderer.class)
public class ThrownTridentRendererMixin {

    @Unique
    private ItemModelResolver twod_projectiles$itemModelResolver;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererProvider.Context context, CallbackInfo ci) {

        this.twod_projectiles$itemModelResolver = context.getItemModelResolver();
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",
                    shift = At.Shift.AFTER
            )
    )
    private void updateScale(ThrownTridentRenderState thrownTridentRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {

        if (TwoDProjectiles.CONFIG.renderTwoDTrident) {

            poseStack.scale(TwoDProjectiles.CONFIG.tridentScale, TwoDProjectiles.CONFIG.tridentScale, TwoDProjectiles.CONFIG.tridentScale);
        }
    }

    @WrapOperation(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;",
                    ordinal = 1
            )
    )
    private Quaternionf modifyZPRotationDegrees(Axis axis, float f, Operation<Quaternionf> original, @Local(argsOnly = true) ThrownTridentRenderState thrownTridentRenderState) {

        float shake = 0.0F;

        if (thrownTridentRenderState instanceof TwoDThrownTridentRenderState twoDThrownTridentRenderState && twoDThrownTridentRenderState.twod_projectiles$getShake() > 0.0F) {

            shake = (-Mth.sin(twoDThrownTridentRenderState.twod_projectiles$getShake() * TwoDProjectiles.CONFIG.tridentShakeSpeedFactor) * twoDThrownTridentRenderState.twod_projectiles$getShake() * TwoDProjectiles.CONFIG.tridentShakePowerFactor) * ((float) Math.PI / 180F);
        }

        if (TwoDProjectiles.CONFIG.renderTwoDTrident) {

            return original.call(axis, thrownTridentRenderState.xRot + shake + TwoDProjectiles.CONFIG.tridentDirection.getDegree());
        }
        else {

            return original.call(axis, thrownTridentRenderState.xRot + shake + 90.0F);
        }
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void renderTwoDTrident(ThrownTridentRenderState thrownTridentRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {

        if (thrownTridentRenderState instanceof TwoDThrownTridentRenderState twoDThrownTridentRenderState) {

            if (TwoDProjectiles.CONFIG.renderTwoDTrident) {

                Quaternionf qY = Axis.YP.rotationDegrees(-90.0F);
                Quaternionf qZ = Axis.ZP.rotationDegrees(TwoDProjectiles.CONFIG.tridentDirection.getDegree());
                Quaternionf qCombined = new Quaternionf(qY).mul(qZ);

                Vector3f axis = new Vector3f();
                qCombined.normalizedPositiveZ(axis);

                poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(twoDThrownTridentRenderState.twod_projectiles$getRoll()), axis));

                float offset = TwoDProjectiles.CONFIG.tridentOffset;
                float radiansZ = (float) Math.toRadians(TwoDProjectiles.CONFIG.tridentDirection.getDegree());
                float offsetX = -(float) Math.cos(radiansZ) * offset;
                float offsetY = (float) Math.sin(radiansZ) * offset;

                poseStack.translate(offsetX, offsetY - 0.125F, 0.0F);

                twoDThrownTridentRenderState.twoDProjectiles$getStack().submit(poseStack, submitNodeCollector, thrownTridentRenderState.lightCoords, OverlayTexture.NO_OVERLAY, thrownTridentRenderState.outlineColor);
            }
            else {

                Quaternionf qY = Axis.YP.rotationDegrees(-90.0F);
                Quaternionf qZ = Axis.ZP.rotationDegrees(90.0F);
                Quaternionf qCombined = new Quaternionf(qY).mul(qZ);

                Vector3f axis = new Vector3f();
                qCombined.normalizedPositiveZ(axis);

                poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(twoDThrownTridentRenderState.twod_projectiles$getRoll()), axis));
            }
        }
    }

    @WrapOperation(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"
            )
    )
    private void cancelRenderToBuffer(OrderedSubmitNodeCollector orderedSubmitNodeCollector, Model<TridentModel> tridentModel, Object object, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayTexture, int k, TextureAtlasSprite textureAtlasSprite, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> original) {

        if (!TwoDProjectiles.CONFIG.renderTwoDTrident) {

            original.call(orderedSubmitNodeCollector, tridentModel, Unit.INSTANCE, poseStack, renderType, lightCoords, overlayTexture, k, null, outlineColor, null);
        }
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/projectile/ThrownTrident;Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;F)V",
            at = @At(value = "TAIL")
    )
    private void extractRenderState(ThrownTrident thrownTrident, ThrownTridentRenderState thrownTridentRenderState, float f, CallbackInfo ci) {

        if (thrownTridentRenderState instanceof TwoDThrownTridentRenderState twoDThrownTridentRenderState) {

            twoDThrownTridentRenderState.twod_projectiles$setShake((float) thrownTrident.shakeTime - f);
            twoDThrownTridentRenderState.twod_projectiles$setRoll(((TwoDRollEntity) thrownTrident).twod_projectiles$getRoll(f));

            this.twod_projectiles$itemModelResolver.updateForNonLiving(twoDThrownTridentRenderState.twoDProjectiles$getStack(), thrownTrident.getWeaponItem(), ItemDisplayContext.GROUND, thrownTrident);
        }
    }
}
