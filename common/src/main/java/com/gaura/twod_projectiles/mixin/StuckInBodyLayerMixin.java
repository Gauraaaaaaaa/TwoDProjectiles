package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StuckInBodyLayer.class)
public class StuckInBodyLayerMixin {

    @Unique
    private final ItemStackRenderState twod_projectiles$stack = new ItemStackRenderState();

    @Inject(method = "submitStuckItem", at = @At(value = "HEAD"), cancellable = true)
    private void renderTwoDStuckArrow(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float f, float g, float h, int outlineColor, CallbackInfo ci) {

        StuckInBodyLayer<?, ?> stuckInBodyLayer = (StuckInBodyLayer<?, ?>) (Object) this;

        if (TwoDProjectiles.CONFIG.renderTwoDArrow && stuckInBodyLayer instanceof ArrowLayer) {

            Minecraft.getInstance().getItemModelResolver().updateForNonLiving(this.twod_projectiles$stack, Items.ARROW.getDefaultInstance(), ItemDisplayContext.GROUND, Minecraft.getInstance().player);

            poseStack.scale(TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale);

            float k = Mth.sqrt(f * f + h * h);
            float yRot = (float) (Math.atan2(f, h) * (double) (180F / (float)Math.PI));
            float xRot = (float) (Math.atan2(g, k) * (double) (180F / (float)Math.PI));

            poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(xRot + TwoDProjectiles.getArrowAngle(Items.ARROW.getDefaultInstance())));

            float offset = TwoDProjectiles.CONFIG.arrowOffset;
            float radiansZ = (float) Math.toRadians(TwoDProjectiles.getArrowAngle(Items.ARROW.getDefaultInstance()));
            float offsetX = -(float) Math.cos(radiansZ) * offset;
            float offsetY = (float) Math.sin(radiansZ) * offset;

            poseStack.translate(offsetX, -0.125F + offsetY, 0.0F);

            this.twod_projectiles$stack.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor);

            ci.cancel();
        }
    }
}
