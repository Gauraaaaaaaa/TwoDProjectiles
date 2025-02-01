package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDProjectileEntityRenderState;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntityRenderer.class)
public abstract class ProjectileEntityRendererMixin {

    @Unique
    private ItemModelManager itemModelManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererFactory.Context context, CallbackInfo ci) {

        this.itemModelManager = context.getItemModelManager();
    }

    @Inject(
            method = "render(Lnet/minecraft/client/render/entity/state/ProjectileEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void render(ProjectileEntityRenderState projectileEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {

        if (projectileEntityRenderState instanceof TwoDProjectileEntityRenderState twoDProjectileEntityRenderState) {

            matrixStack.push();
            matrixStack.scale(TwoDProjectiles.CONFIG.arrow_scale, TwoDProjectiles.CONFIG.arrow_scale, TwoDProjectiles.CONFIG.arrow_scale);

            float f = 0.0F;
            if (projectileEntityRenderState.shake > 0.0F) {

                f = (-MathHelper.sin(projectileEntityRenderState.shake * 3.0F) * projectileEntityRenderState.shake * TwoDProjectiles.CONFIG.arrow_shake_factor) * ((float)Math.PI / 180F);
            }

            if (twoDProjectileEntityRenderState.twoDProjectiles$isArrowFromCrossbow() && TwoDProjectiles.CONFIG.flat_arrow_with_crossbow) {

                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(projectileEntityRenderState.yaw + TwoDProjectiles.CONFIG.crossbow_y_rotation));
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(projectileEntityRenderState.pitch + TwoDProjectiles.CONFIG.crossbow_x_rotation));
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(TwoDProjectiles.CONFIG.crossbow_z_rotation + f));
                matrixStack.translate(TwoDProjectiles.CONFIG.crossbow_x_translation, TwoDProjectiles.CONFIG.crossbow_y_translation, TwoDProjectiles.CONFIG.crossbow_z_translation);
            }
            else {

                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(projectileEntityRenderState.yaw + TwoDProjectiles.CONFIG.bow_y_rotation));
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(projectileEntityRenderState.pitch + TwoDProjectiles.CONFIG.bow_z_rotation + f));
                matrixStack.translate(TwoDProjectiles.CONFIG.bow_x_translation, TwoDProjectiles.CONFIG.bow_y_translation, TwoDProjectiles.CONFIG.bow_z_translation);
            }

            twoDProjectileEntityRenderState.twoDProjectiles$getStack().render(matrixStack, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV);
            matrixStack.pop();
        }

        ci.cancel();
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/projectile/PersistentProjectileEntity;Lnet/minecraft/client/render/entity/state/ProjectileEntityRenderState;F)V", at = @At("TAIL"))
    private void updateRenderState(PersistentProjectileEntity persistentProjectileEntity, ProjectileEntityRenderState projectileEntityRenderState, float f, CallbackInfo ci) {

        if (projectileEntityRenderState instanceof TwoDProjectileEntityRenderState twoDProjectileEntityRenderState) {

            twoDProjectileEntityRenderState.twoDProjectiles$setArrowFromCrossbow(persistentProjectileEntity.getDataTracker().get(TwoDProjectiles.ARROW_FROM_CROSSBOW));
            this.itemModelManager.updateForNonLivingEntity(twoDProjectileEntityRenderState.twoDProjectiles$getStack(), persistentProjectileEntity.getDataTracker().get(TwoDProjectiles.ARROW_ITEM), ModelTransformationMode.GROUND, persistentProjectileEntity);
        }
    }
}
