package com.gaura.twod_projectiles;

import com.gaura.twod_projectiles.config.TwoDProjectilesConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TwoDProjectiles {

    public static final String MOD_ID = "twod_projectiles";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static TwoDProjectilesConfig CONFIG = new TwoDProjectilesConfig();

    public static final EntityDataAccessor<ItemStack> ARROW_ITEM = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.ITEM_STACK);

    public static final EntityDataAccessor<Boolean> ARROW_FROM_CROSSBOW = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BOOLEAN);

    public static void init() {

        AutoConfig.register(TwoDProjectilesConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(TwoDProjectilesConfig.class).getConfig();
    }
}
