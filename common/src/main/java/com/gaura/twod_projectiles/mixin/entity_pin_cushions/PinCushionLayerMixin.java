package com.gaura.twod_projectiles.mixin.entity_pin_cushions;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
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
@Mixin(PinCushionLayer.class)
public class PinCushionLayerMixin {

    @Unique
    private final ItemStackRenderState twod_projectiles$stack = new ItemStackRenderState();

    @Inject(method = "renderStuckItem", at = @At(value = "HEAD"), cancellable = true)
    private void renderTwoDStuckArrow(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f, float g, float h, CallbackInfo ci) {

        @SuppressWarnings("unchecked")
        PinCushionLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> pinCushionLayer = (PinCushionLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>>) (Object) this;

        if (TwoDProjectiles.CONFIG.renderTwoDArrow && pinCushionLayer instanceof PinCushionLayer.ArrowLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>>) {

            Minecraft.getInstance().getItemModelResolver().updateForNonLiving(this.twod_projectiles$stack, Items.ARROW.getDefaultInstance(), ItemDisplayContext.GROUND, Minecraft.getInstance().player);

            poseStack.scale(TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale);

            float j = Mth.sqrt(f * f + h * h);
            float yRot = (float) (Math.atan2(f, h) * (double) (180F / (float) Math.PI));
            float xRot = (float) (Math.atan2(g, j) * (double) (180F / (float) Math.PI));

            poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(xRot + TwoDProjectiles.getArrowAngle(Items.ARROW.getDefaultInstance())));

            float offset = TwoDProjectiles.CONFIG.arrowOffset;
            float radiansZ = (float) Math.toRadians(TwoDProjectiles.getArrowAngle(Items.ARROW.getDefaultInstance()));
            float offsetX = -(float) Math.cos(radiansZ) * offset;
            float offsetY = (float) Math.sin(radiansZ) * offset;

            poseStack.translate(offsetX, -0.125F + offsetY, 0.0F);

            this.twod_projectiles$stack.render(poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY);

            ci.cancel();
        }
    }
}
