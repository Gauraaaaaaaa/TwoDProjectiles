package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDFireworkRocketEntityRenderState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FireworkRocketEntityRenderer;
import net.minecraft.client.render.entity.state.FireworkRocketEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntityRenderer.class)
public class FireworkRocketEntityRendererMixin {

    @Inject(
            method = "render(Lnet/minecraft/client/render/entity/state/FireworkRocketEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;push()V",
                    shift = At.Shift.AFTER
            )
    )
    private void renderFireworkRocketEntity(FireworkRocketEntityRenderState fireworkRocketEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {

        matrixStack.scale(TwoDProjectiles.CONFIG.firework_rocket_scale, TwoDProjectiles.CONFIG.firework_rocket_scale, TwoDProjectiles.CONFIG.firework_rocket_scale);

        if (fireworkRocketEntityRenderState.shotAtAngle && fireworkRocketEntityRenderState instanceof TwoDFireworkRocketEntityRenderState fireworkRocketVelocityEntityRenderState) {

            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(fireworkRocketVelocityEntityRenderState.twoDProjectiles$getYaw() + TwoDProjectiles.CONFIG.firework_rocket_y_rotation));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(fireworkRocketVelocityEntityRenderState.twoDProjectiles$getPitch() + TwoDProjectiles.CONFIG.firework_rocket_x_rotation));
        }

        matrixStack.translate(TwoDProjectiles.CONFIG.firework_rocket_x_translation, TwoDProjectiles.CONFIG.firework_rocket_y_translation, TwoDProjectiles.CONFIG.firework_rocket_z_translation);
    }

    @Redirect(
            method = "render(Lnet/minecraft/client/render/entity/state/FireworkRocketEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
                    ordinal = 0
            )
    )
    private void disableRotation(MatrixStack matrixStack, Quaternionf quaternion) {

        // Does nothing to disable the firework rocket facing the player
    }

    @Inject(
            method = "updateRenderState(Lnet/minecraft/entity/projectile/FireworkRocketEntity;Lnet/minecraft/client/render/entity/state/FireworkRocketEntityRenderState;F)V",
            at = @At("TAIL")
    )
    private void updateRenderState(FireworkRocketEntity fireworkRocketEntity, FireworkRocketEntityRenderState fireworkRocketEntityRenderState, float f, CallbackInfo ci) {

        if (fireworkRocketEntityRenderState instanceof TwoDFireworkRocketEntityRenderState fireworkRocketVelocityEntityRenderState) {

            fireworkRocketVelocityEntityRenderState.twoDProjectiles$setYaw(fireworkRocketEntity.getYaw());
            fireworkRocketVelocityEntityRenderState.twoDProjectiles$setPitch(fireworkRocketEntity.getPitch());
        }
    }
}
