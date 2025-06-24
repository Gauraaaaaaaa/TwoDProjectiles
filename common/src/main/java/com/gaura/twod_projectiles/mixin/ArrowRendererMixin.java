package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import com.gaura.twod_projectiles.util.TwoDArrowRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArrowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(ArrowRenderer.class)
public class ArrowRendererMixin {

    @Unique
    private ItemModelResolver twod_projectiles$itemModelResolver;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererProvider.Context context, CallbackInfo ci) {

        this.twod_projectiles$itemModelResolver = context.getItemModelResolver();
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

        poseStack.scale(TwoDProjectiles.CONFIG.arrow_scale, TwoDProjectiles.CONFIG.arrow_scale, TwoDProjectiles.CONFIG.arrow_scale);
    }

    @Redirect(
            method = "render(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 1
            )
    )
    private void cancelRotationDegrees(PoseStack instance, Quaternionf quaternionf) {}

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

        poseStack.mulPose(Axis.ZP.rotationDegrees(arrowRenderState.xRot + ((TwoDArrowRenderState) arrowRenderState).twod_projectiles$getArrowAngle()));

        Quaternionf qY = Axis.YP.rotationDegrees(-90.0F);
        Quaternionf qZ = Axis.ZP.rotationDegrees(((TwoDArrowRenderState) arrowRenderState).twod_projectiles$getArrowAngle());
        Quaternionf qCombined = new Quaternionf(qY).mul(qZ);

        Vector3f axis = new Vector3f();
        qCombined.normalizedPositiveZ(axis);

        if (((TwoDArrowRenderState) arrowRenderState).twoDProjectiles$isArrowFromCrossbow() && TwoDProjectiles.CONFIG.flat_arrow_with_crossbow) {

            poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(-90.0F), axis));
        }

        poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(((TwoDArrowRenderState) arrowRenderState).twod_projectiles$getRoll()), axis));

        float offset = TwoDProjectiles.CONFIG.arrow_offset;
        float radiansZ = (float) Math.toRadians(((TwoDArrowRenderState) arrowRenderState).twod_projectiles$getArrowAngle());
        float offsetX = -(float) Math.cos(radiansZ) * offset;
        float offsetY = (float) Math.sin(radiansZ) * offset;

        poseStack.translate(offsetX, offsetY - 0.125F, 0.0F);

        ((TwoDArrowRenderState) arrowRenderState).twoDProjectiles$getStack().render(poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY);
    }

    @Redirect(
            method = "render(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ArrowModel;setupAnim(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;)V"
            )
    )
    private void cancelSetupAnim(ArrowModel instance, ArrowRenderState arrowRenderState) {}

    @Redirect(
            method = "render(Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ArrowModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"
            )
    )
    private void cancelRenderToBuffer(ArrowModel instance, PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j) {}

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/projectile/AbstractArrow;Lnet/minecraft/client/renderer/entity/state/ArrowRenderState;F)V",
            at = @At("TAIL")
    )
    private void updateRenderState(AbstractArrow abstractArrow, ArrowRenderState arrowRenderState, float f, CallbackInfo ci) {

        if (arrowRenderState.shake > 0.0F) {

            arrowRenderState.xRot += (-Mth.sin(arrowRenderState.shake * TwoDProjectiles.CONFIG.arrow_speed_shake_factor) * arrowRenderState.shake * TwoDProjectiles.CONFIG.arrow_shake_factor) * ((float)Math.PI / 180F);
        }

        if (arrowRenderState instanceof TwoDArrowRenderState twoDArrowRenderState) {

            twoDArrowRenderState.twod_projectiles$setRoll(((TwoDRollEntity) abstractArrow).twod_projectiles$getRoll(f));

            float angle = Arrays.stream(TwoDProjectiles.CONFIG.arrow_direction_list)
                    .filter(arrowDirection -> abstractArrow.getEntityData().get(TwoDProjectiles.ARROW_ITEM).getItemHolder().is(ResourceLocation.parse(arrowDirection.arrow)))
                    .map(arrowDirection -> arrowDirection.direction.getDegree())
                    .findFirst()
                    .orElse(-45.0F);

            twoDArrowRenderState.twod_projectiles$setArrowAngle(angle);

            twoDArrowRenderState.twoDProjectiles$setArrowFromCrossbow(abstractArrow.getEntityData().get(TwoDProjectiles.ARROW_FROM_CROSSBOW));
            this.twod_projectiles$itemModelResolver.updateForNonLiving(twoDArrowRenderState.twoDProjectiles$getStack(), abstractArrow.getEntityData().get(TwoDProjectiles.ARROW_ITEM), ItemDisplayContext.GROUND, abstractArrow);
        }
    }
}
