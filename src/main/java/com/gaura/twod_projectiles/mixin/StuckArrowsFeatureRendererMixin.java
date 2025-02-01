package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.util.TwoDStuckArrowsFeatureRenderState;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.StuckArrowsFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StuckArrowsFeatureRenderer.class)
public class StuckArrowsFeatureRendererMixin implements TwoDStuckArrowsFeatureRenderState {

    @Unique
    private ItemRenderer itemRenderer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(LivingEntityRenderer<?, PlayerEntityRenderState, PlayerEntityModel> entityRenderer, EntityRendererFactory.Context context, CallbackInfo ci) {

        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public ItemRenderer twoDProjectiles$getItemRenderer() {

        return itemRenderer;
    }
}
