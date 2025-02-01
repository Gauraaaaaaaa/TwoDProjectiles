package com.gaura.twod_projectiles;

import com.gaura.twod_projectiles.config.TwoDProjectilesConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwoDProjectiles implements ModInitializer {

	public static final String MOD_ID = "twod_projectiles";

	public static TwoDProjectilesConfig CONFIG = new TwoDProjectilesConfig();

	public static final TrackedData<ItemStack> ARROW_ITEM = DataTracker.registerData(PersistentProjectileEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

	public static final TrackedData<Boolean> ARROW_FROM_CROSSBOW = DataTracker.registerData(PersistentProjectileEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	@Override
	public void onInitialize() {

		AutoConfig.register(TwoDProjectilesConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(TwoDProjectilesConfig.class).getConfig();
	}
}