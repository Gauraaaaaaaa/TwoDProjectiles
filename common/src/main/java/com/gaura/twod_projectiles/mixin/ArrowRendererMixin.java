package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
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
import org.spongepowered.asm.mixin.injection.*;
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
            method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",
                    shift = At.Shift.AFTER
            )
    )
    private void updateScale(AbstractArrow abstractArrow, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {

        if (TwoDProjectiles.CONFIG.renderTwoDArrow) {

            poseStack.scale(TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale);
        }
    }

    @Redirect(
            method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;",
                    ordinal = 1
            )
    )
    private Quaternionf modifyZPRotationDegrees(Axis axis, float f, @Local(argsOnly = true) AbstractArrow abstractArrow) {

        if (TwoDProjectiles.CONFIG.renderTwoDArrow) {

            ItemStack itemStack = ((AbstractArrowInvoker) abstractArrow).invokeGetPickupItem();

            if (TwoDProjectiles.CONFIG.renderTippedArrow && abstractArrow instanceof Arrow arrow && arrow.getColor() != -1) {

                itemStack = Items.TIPPED_ARROW.getDefaultInstance();
                itemStack.set(DataComponents.POTION_CONTENTS, arrow.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(arrow.getColor()), List.of())));
            }

            return axis.rotationDegrees(f + TwoDProjectiles.getArrowAngle(itemStack));
        }
        else {

            return axis.rotationDegrees(f);
        }
    }

    @ModifyConstant(
            method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            constant = @Constant(floatValue = 3.0F)
    )
    private float modifyShakeSpeedFactor(float constant) {

        return TwoDProjectiles.CONFIG.arrowShakeSpeedFactor;
    }

    @Redirect(
            method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;sin(F)F"
            )
    )
    private float modifyShakePowerFactor(float f) {

        return (-Mth.sin(f) * TwoDProjectiles.CONFIG.arrowShakePowerFactor) * (Mth.PI / 180F);
    }

    @Redirect(
            method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 3
            )
    )
    private void cancelXPRotationDegreesFirst(PoseStack poseStack, Quaternionf quaternionf) {

        if (!TwoDProjectiles.CONFIG.renderTwoDArrow) {

            poseStack.mulPose(quaternionf);
        }
    }

    @Redirect(
            method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"
            )
    )
    private void cancelScale(PoseStack poseStack, float x, float y, float z) {

        if (!TwoDProjectiles.CONFIG.renderTwoDArrow) {

            poseStack.scale(x, y, z);
        }
    }

    @Redirect(
            method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"
            )
    )
    private void cancelTranslate(PoseStack poseStack, float x, float y, float z, @Local(argsOnly = true) AbstractArrow abstractArrow) {

        if (!TwoDProjectiles.CONFIG.renderTwoDArrow) {

            poseStack.translate(x, y, z);
        }
    }

    @Inject(
            method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
                    shift = At.Shift.AFTER
            )
    )
    private void renderTwoDArrow(AbstractArrow abstractArrow, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {

        if (TwoDProjectiles.CONFIG.renderTwoDArrow) {

            ItemStack itemStack = ((AbstractArrowInvoker) abstractArrow).invokeGetPickupItem();

            if (TwoDProjectiles.CONFIG.renderTippedArrow && abstractArrow instanceof Arrow arrow && arrow.getColor() != -1) {

                itemStack = Items.TIPPED_ARROW.getDefaultInstance();
                itemStack.set(DataComponents.POTION_CONTENTS, arrow.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(arrow.getColor()), List.of())));
            }

            float angle = TwoDProjectiles.getArrowAngle(itemStack);

            Quaternionf qY = Axis.YP.rotationDegrees(-90.0F);
            Quaternionf qZ = Axis.ZP.rotationDegrees(angle);
            Quaternionf qCombined = new Quaternionf(qY).mul(qZ);

            Vector3f axis = new Vector3f();
            qCombined.normalizedPositiveZ(axis);

            poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(((TwoDRollEntity) abstractArrow).twod_projectiles$getRoll(g)), axis));

            float offset = TwoDProjectiles.CONFIG.arrowOffset;
            float radiansZ = (float) Math.toRadians(angle);
            float offsetX = -(float) Math.cos(radiansZ) * offset;
            float offsetY = (float) Math.sin(radiansZ) * offset;

            poseStack.translate(offsetX, offsetY - 0.125F, 0.0F);

            this.twod_projectiles$itemRenderer.renderStatic(itemStack, ItemDisplayContext.GROUND, i, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, abstractArrow.level(), abstractArrow.getId());
        }
        else {

            Quaternionf qY = Axis.YP.rotationDegrees(-90.0F);
            Quaternionf qZ = Axis.ZP.rotationDegrees(0.0F);
            Quaternionf qCombined = new Quaternionf(qY).mul(qZ);

            Vector3f axis = new Vector3f();
            qCombined.normalizedPositiveZ(axis);

            poseStack.mulPose(new Quaternionf().rotationAxis((float) Math.toRadians(((TwoDRollEntity) abstractArrow).twod_projectiles$getRoll(g)), axis));
        }
    }

    @Redirect(
            method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 4
            )
    )
    private void cancelXPRotationDegreesSecond(PoseStack poseStack, Quaternionf quaternionf) {

        if (!TwoDProjectiles.CONFIG.renderTwoDArrow) {

            poseStack.mulPose(quaternionf);
        }
    }

    @Inject(method = "vertex", at = @At("HEAD"), cancellable = true)
    private void cancelVertex(PoseStack.Pose pose, VertexConsumer vertexConsumer, int i, int j, int k, float f, float g, int l, int m, int n, int o, CallbackInfo ci) {

        if (TwoDProjectiles.CONFIG.renderTwoDArrow) {

            ci.cancel();
        }
    }
}
