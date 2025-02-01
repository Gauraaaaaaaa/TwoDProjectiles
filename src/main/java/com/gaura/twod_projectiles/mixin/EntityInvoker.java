package com.gaura.twod_projectiles.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityInvoker {

    @Invoker("getRegistryManager")
    DynamicRegistryManager invokeGetRegistryManager();
}
