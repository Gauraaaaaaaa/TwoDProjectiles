package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDStuckArrowsFeatureRenderState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.StuckObjectsFeatureRenderer;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StuckObjectsFeatureRenderer.class)
public class StuckObjectsFeatureRendererMixin {

    @Unique
    private final ItemRenderState stack = new ItemRenderState();

    @Inject(method = "renderObject", at = @At(value = "HEAD"), cancellable = true)
    private void renderObject(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, float f, float directionX, float directionY, CallbackInfo ci) {

        StuckObjectsFeatureRenderer stuckObjectsFeatureRenderer = (StuckObjectsFeatureRenderer) (Object) this;

        if (stuckObjectsFeatureRenderer instanceof TwoDStuckArrowsFeatureRenderState twoDStuckArrowsFeatureRenderState) {

            twoDStuckArrowsFeatureRenderState.twoDProjectiles$getItemModelManager().updateForNonLivingEntity(this.stack, Items.ARROW.getDefaultStack(), ModelTransformationMode.GROUND, MinecraftClient.getInstance().player);

            matrixStack.scale(TwoDProjectiles.CONFIG.arrow_stuck_in_player_scale, TwoDProjectiles.CONFIG.arrow_stuck_in_player_scale, TwoDProjectiles.CONFIG.arrow_stuck_in_player_scale);

            float g = MathHelper.sqrt(f * f + directionY * directionY);
            float h = (float)(Math.atan2(f, directionY) * 180.0F / (float)Math.PI);
            float i = (float)(Math.atan2(directionX, g) * 180.0F / (float)Math.PI);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(h + TwoDProjectiles.CONFIG.bow_y_rotation));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(i + TwoDProjectiles.CONFIG.bow_z_rotation));

            matrixStack.translate(TwoDProjectiles.CONFIG.bow_x_translation, TwoDProjectiles.CONFIG.bow_y_translation, TwoDProjectiles.CONFIG.bow_z_translation);

            this.stack.render(matrixStack, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV);

            ci.cancel();
        }
    }
}