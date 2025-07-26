package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StuckInBodyLayer.class)
public class StuckInBodyLayerMixin {

    @Unique
    @Nullable
    private BakedModel twod_projectiles$bakedModel;

    @Inject(method = "renderStuckItem", at = @At(value = "HEAD"), cancellable = true)
    private void renderTwoDStuckArrow(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f, float g, float h, CallbackInfo ci) {

        StuckInBodyLayer<? extends PlayerModel> stuckInBodyLayer = (StuckInBodyLayer<? extends PlayerModel>) (Object) this;

        if (TwoDProjectiles.CONFIG.renderTwoDArrow && stuckInBodyLayer instanceof ArrowLayer) {

            LocalPlayer player = Minecraft.getInstance().player;

            if (player != null) {

                this.twod_projectiles$bakedModel = Minecraft.getInstance().getItemRenderer().getModel(Items.ARROW.getDefaultInstance(), player.level(), null, player.getId());
            }

            poseStack.scale(TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale, TwoDProjectiles.CONFIG.arrowScale);

            float j = Mth.sqrt(f * f + h * h);
            float k = (float) (Math.atan2(f, h) * (double) (180F / (float) Math.PI));
            float l = (float) (Math.atan2(g, j) * (double) (180F / (float) Math.PI));

            poseStack.mulPose(Axis.YP.rotationDegrees(k - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(l + TwoDProjectiles.getArrowAngle(Items.ARROW.getDefaultInstance())));

            float offset = TwoDProjectiles.CONFIG.arrowOffset;
            float radiansZ = (float) Math.toRadians(TwoDProjectiles.getArrowAngle(Items.ARROW.getDefaultInstance()));
            float offsetX = -(float) Math.cos(radiansZ) * offset;
            float offsetY = (float) Math.sin(radiansZ) * offset;

            poseStack.translate(offsetX, -0.125F + offsetY, 0.0F);

            Minecraft.getInstance().getItemRenderer().render(Items.ARROW.getDefaultInstance(), ItemDisplayContext.GROUND, false, poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY, this.twod_projectiles$bakedModel);

            ci.cancel();
        }
    }
}
