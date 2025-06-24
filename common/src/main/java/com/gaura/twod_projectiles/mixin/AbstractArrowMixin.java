package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import com.gaura.twod_projectiles.util.TwoDRollEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin implements TwoDRollEntity {

    @Shadow
    protected abstract ItemStack getDefaultPickupItem();

    @Shadow
    protected abstract boolean isInGround();

    @Unique
    private float twod_projectiles$roll = 0.0F;

    @Inject(
            method = "<init>(Lnet/minecraft/world/entity/EntityType;DDDLnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("TAIL")
    )
    private void initAbstractArrow(EntityType<? extends AbstractArrow> entityType, double d, double e, double f, Level level, ItemStack itemStack, ItemStack itemStack2, CallbackInfo ci) {

        AbstractArrow abstractArrow = (AbstractArrow) (Object) this;
        abstractArrow.getEntityData().set(TwoDProjectiles.ARROW_ITEM, TwoDProjectiles.CONFIG.render_tipped_arrow ? itemStack.copy() : this.getDefaultPickupItem());
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;isCritArrow()Z"
            )
    )
    private boolean removeCriticalParticles(AbstractArrow abstractArrow) {

        return abstractArrow.isCritArrow() && TwoDProjectiles.CONFIG.render_critical_particles;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {

        AbstractArrow abstractArrow = (AbstractArrow) (Object) this;

        if (!this.isInGround()) {

            twod_projectiles$roll += (float) (TwoDProjectiles.CONFIG.arrow_roll * abstractArrow.getDeltaMovement().length());
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {

        builder.define(TwoDProjectiles.ARROW_FROM_CROSSBOW, false);
        builder.define(TwoDProjectiles.ARROW_ITEM, this.getDefaultPickupItem());
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {

        AbstractArrow abstractArrow = (AbstractArrow) (Object) this;
        compoundTag.putBoolean("ArrowFromCrossbow", abstractArrow.getEntityData().get(TwoDProjectiles.ARROW_FROM_CROSSBOW));
        compoundTag.put("ArrowItem", abstractArrow.getEntityData().get(TwoDProjectiles.ARROW_ITEM).save(((EntityInvoker) abstractArrow).invokeRegistryAccess()));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {

        AbstractArrow abstractArrow = (AbstractArrow) (Object) this;
        abstractArrow.getEntityData().set(TwoDProjectiles.ARROW_FROM_CROSSBOW, compoundTag.getBoolean("ArrowFromCrossbow"));

        if (compoundTag.contains("ArrowItem", Tag.TAG_COMPOUND)) {

            abstractArrow.getEntityData().set(TwoDProjectiles.ARROW_ITEM, ItemStack.parse(((EntityInvoker) abstractArrow).invokeRegistryAccess(), compoundTag.getCompound("ArrowItem")).orElseGet(this::getDefaultPickupItem));
        }
        else {

            abstractArrow.getEntityData().set(TwoDProjectiles.ARROW_ITEM, this.getDefaultPickupItem());
        }
    }

    @Override
    public float twod_projectiles$getRoll(float f) {

        if (this.isInGround()) return twod_projectiles$roll % 360.0F;

        double speed = ((AbstractArrow) (Object) this).getDeltaMovement().length();
        float interpolated = twod_projectiles$roll + (float) (TwoDProjectiles.CONFIG.arrow_roll * speed * f);
        return interpolated % 360.0F;
    }
}
