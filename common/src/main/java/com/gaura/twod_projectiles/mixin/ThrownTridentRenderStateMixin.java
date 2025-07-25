package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.util.TwoDThrownTridentRenderState;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ThrownTridentRenderState.class)
public class ThrownTridentRenderStateMixin implements TwoDThrownTridentRenderState {

    @Unique
    private final ItemStackRenderState twod_projectiles$stack = new ItemStackRenderState();

    @Unique
    private float twod_projectiles$shake;

    @Unique
    private float twod_projectiles$roll;

    @Unique
    private float twod_projectiles$tridentAngle;

    @Override
    public ItemStackRenderState twoDProjectiles$getStack() {

        return twod_projectiles$stack;
    }

    @Override
    public float twod_projectiles$getShake() {

        return twod_projectiles$shake;
    }

    @Override
    public void twod_projectiles$setShake(float shake) {

        this.twod_projectiles$shake = shake;
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
    public float twod_projectiles$getTridentAngle() {

        return twod_projectiles$tridentAngle;
    }

    @Override
    public void twod_projectiles$setTridentAngle(float arrowAngle) {

        this.twod_projectiles$tridentAngle = arrowAngle;
    }
}
