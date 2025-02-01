package com.gaura.twod_projectiles.mixin;

import com.gaura.twod_projectiles.TwoDProjectiles;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {

    @Shadow
    protected abstract ItemStack getDefaultItemStack();

    @Inject(
            method = "<init>(Lnet/minecraft/entity/EntityType;DDDLnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V",
            at = @At("TAIL")
    )
    private void initPersistentProjectileEntityMixin(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon, CallbackInfo ci) {

        PersistentProjectileEntity persistentProjectile = (PersistentProjectileEntity) (Object) this;
        persistentProjectile.getDataTracker().set(TwoDProjectiles.ARROW_ITEM, stack.copy());
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {

        builder.add(TwoDProjectiles.ARROW_FROM_CROSSBOW, false);
        builder.add(TwoDProjectiles.ARROW_ITEM, this.getDefaultItemStack());
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {

        PersistentProjectileEntity persistentProjectile = (PersistentProjectileEntity) (Object) this;
        nbt.putBoolean("ArrowFromCrossbow", persistentProjectile.getDataTracker().get(TwoDProjectiles.ARROW_FROM_CROSSBOW));
        nbt.put("ArrowItem", persistentProjectile.getDataTracker().get(TwoDProjectiles.ARROW_ITEM).toNbt(((EntityInvoker) persistentProjectile).invokeGetRegistryManager()));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {

        PersistentProjectileEntity persistentProjectile = (PersistentProjectileEntity) (Object) this;
        persistentProjectile.getDataTracker().set(TwoDProjectiles.ARROW_FROM_CROSSBOW, nbt.getBoolean("ArrowFromCrossbow"));

        if (nbt.contains("ArrowItem", NbtElement.COMPOUND_TYPE)) {

            persistentProjectile.getDataTracker().set(TwoDProjectiles.ARROW_ITEM, ItemStack.fromNbt(((EntityInvoker) persistentProjectile).invokeGetRegistryManager(), nbt.getCompound("ArrowItem")).orElseGet(this::getDefaultItemStack));
        }
        else {

            persistentProjectile.getDataTracker().set(TwoDProjectiles.ARROW_ITEM, getDefaultItemStack());
        }
    }
}
