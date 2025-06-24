package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDArrowLayerRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(StuckInBodyLayer.class)
public class StuckInBodyLayerMixin {

    @Unique
    private final ItemStackRenderState twod_projectiles$stack = new ItemStackRenderState();

    @Inject(method = "renderStuckItem", at = @At(value = "HEAD"))
    private void updateModelAndScale(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f, float g, float h, CallbackInfo ci) {

        StuckInBodyLayer<? extends PlayerModel> stuckObjectsFeatureRenderer = (StuckInBodyLayer<? extends PlayerModel>) (Object) this;

        if (stuckObjectsFeatureRenderer instanceof TwoDArrowLayerRenderState twoDArrowLayerRenderState) {

            twoDArrowLayerRenderState.twoDProjectiles$getItemModelResolver().updateForNonLiving(this.twod_projectiles$stack, Items.ARROW.getDefaultInstance(), ItemDisplayContext.GROUND, Minecraft.getInstance().player);

            poseStack.scale(TwoDProjectiles.CONFIG.arrow_scale, TwoDProjectiles.CONFIG.arrow_scale, TwoDProjectiles.CONFIG.arrow_scale);
        }
    }

    @Redirect(
            method = "renderStuckItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;",
                    ordinal = 1
            )
    )
    private Quaternionf redirectRotationDegrees(Axis instance, float f) {

        float angle = Arrays.stream(TwoDProjectiles.CONFIG.arrow_direction_list)
                .filter(arrowDirection -> Items.ARROW.getDefaultInstance().getItemHolder().is(ResourceLocation.parse(arrowDirection.arrow)))
                .map(arrowDirection -> arrowDirection.direction.getDegree())
                .findFirst()
                .orElse(-45.0F);

        return Axis.YP.rotationDegrees(f + angle);
    }

    @Inject(
            method = "renderStuckItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void renderTwoDArrow(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f, float g, float h, CallbackInfo ci) {

        float angle = Arrays.stream(TwoDProjectiles.CONFIG.arrow_direction_list)
                .filter(arrowDirection -> Items.ARROW.getDefaultInstance().getItemHolder().is(ResourceLocation.parse(arrowDirection.arrow)))
                .map(arrowDirection -> arrowDirection.direction.getDegree())
                .findFirst()
                .orElse(-45.0F);

        float offset = TwoDProjectiles.CONFIG.arrow_offset;
        float radiansZ = (float) Math.toRadians(angle);
        float offsetX = -(float) Math.cos(radiansZ) * offset;
        float offsetY = (float) Math.sin(radiansZ) * offset;

        poseStack.translate(offsetX, -0.125F + offsetY, 0.0F);

        this.twod_projectiles$stack.render(poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY);
    }

    @Redirect(
            method = "renderStuckItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/Model;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"
            )
    )
    private void redirectRenderToBuffer(Model instance, PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j) {}
}
