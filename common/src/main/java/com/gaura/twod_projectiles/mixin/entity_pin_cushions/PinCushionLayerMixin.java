package com.gaura.twod_projectiles.mixin.entity_pin_cushions;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import traben.entity_pin_cushions.PinCushionLayer;

@Pseudo
@Mixin(PinCushionLayer.ArrowLayer.class)
public class PinCushionLayerMixin {

    @Unique
    private ItemRenderer twod_projectiles$itemRenderer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererProvider.Context context, LivingEntityRenderer<LivingEntity, PlayerModel<LivingEntity>> livingEntityRenderer, CallbackInfo ci) {

        this.twod_projectiles$itemRenderer = context.getItemRenderer();
    }

    @Inject(method = "renderStuckItem", at = @At(value = "HEAD"), cancellable = true)
    private void renderTwoDStuckArrow(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Entity entity, float f, float g, float h, float j, CallbackInfo ci) {

        if (TwoDProjectiles.CONFIG.renderTwoDArrow) {

            poseStack.scale(TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale);

            float k = Mth.sqrt(f * f + h * h);
            float yRot = (float) (Math.atan2(f, h) * (double) (180F / (float) Math.PI));
            float xRot = (float) (Math.atan2(g, k) * (double) (180F / (float) Math.PI));

            poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(xRot + TwoDProjectiles.getArrowAngle(Items.ARROW.getDefaultInstance())));

            float offset = TwoDProjectiles.CONFIG.arrowOffset;
            float radiansZ = (float) Math.toRadians(TwoDProjectiles.getArrowAngle(Items.ARROW.getDefaultInstance()));
            float offsetX = -(float) Math.cos(radiansZ) * offset;
            float offsetY = (float) Math.sin(radiansZ) * offset;

            poseStack.translate(offsetX, -0.125F + offsetY, 0.0F);

            twod_projectiles$itemRenderer.renderStatic(Items.ARROW.getDefaultInstance(), ItemDisplayContext.GROUND, i, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, entity.level(), entity.getId());

            ci.cancel();
        }
    }
}