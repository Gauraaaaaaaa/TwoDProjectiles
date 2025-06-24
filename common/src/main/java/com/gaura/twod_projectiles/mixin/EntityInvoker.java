package com.gaura.twod_projectiles.mixin;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityInvoker {

    @Invoker("registryAccess")
    RegistryAccess invokeRegistryAccess();
}