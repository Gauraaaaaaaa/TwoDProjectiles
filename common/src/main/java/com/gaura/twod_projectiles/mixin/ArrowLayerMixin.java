package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.util.TwoDArrowLayerRenderState;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowLayer.class)
public class ArrowLayerMixin implements TwoDArrowLayerRenderState {

    @Unique
    private ItemModelResolver twod_projectiles$itemModelResolver;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(LivingEntityRenderer<?, PlayerRenderState, PlayerModel> livingEntityRenderer, EntityRendererProvider.Context context, CallbackInfo ci) {

        this.twod_projectiles$itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public ItemModelResolver twoDProjectiles$getItemModelResolver() {

        return twod_projectiles$itemModelResolver;
    }
}
