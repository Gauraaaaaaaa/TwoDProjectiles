package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin implements TwoDRollEntity {

    @Unique
    private float twod_projectiles$roll = 0.0F;

    @Unique
    private int twod_projectiles$random;

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("RETURN"))
    private void onInit(EntityType<FireworkRocketEntity> entityType, Level level, CallbackInfo ci) {

        TwoDProjectiles.LOGGER.info("FIREWORK INIT");
        this.twod_projectiles$random = RandomSource.create().nextBoolean() ? 1 : -1;
        TwoDProjectiles.LOGGER.info("{}", this.twod_projectiles$random);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {

        this.twod_projectiles$roll += this.twod_projectiles$random * TwoDProjectiles.CONFIG.fireworkRocketRoll;
    }

    @Override
    public float twod_projectiles$getRoll(float f) {

        float interpolated = this.twod_projectiles$roll + (this.twod_projectiles$random * TwoDProjectiles.CONFIG.fireworkRocketRoll * f);
        return interpolated % 360.0F;
    }
}
