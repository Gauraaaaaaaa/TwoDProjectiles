package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.util.TwoDArrowRenderState;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ArrowRenderState.class)
public class ArrowRenderStateMixin implements TwoDArrowRenderState {

    @Unique
    private final ItemStackRenderState twod_projectiles$stack = new ItemStackRenderState();

    @Unique
    private float twod_projectiles$roll;

    @Unique
    private float twod_projectiles$arrowAngle;

    @Override
    public ItemStackRenderState twoDProjectiles$getStack() {

        return twod_projectiles$stack;
    }

    @Override
    public float twod_projectiles$getRoll() {

        return twod_projectiles$roll;
    }

    @Override
    public void twod_projectiles$setRoll(float roll) {

        this.twod_projectiles$roll = roll;
    }

    @Override
    public float twod_projectiles$getArrowAngle() {

        return twod_projectiles$arrowAngle;
    }

    @Override
    public void twod_projectiles$setArrowAngle(float arrowAngle) {

        this.twod_projectiles$arrowAngle = arrowAngle;
    }
}
