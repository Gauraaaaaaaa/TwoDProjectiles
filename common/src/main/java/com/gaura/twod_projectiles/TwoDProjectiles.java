package com.gaura.twod_projectiles;

import com.gaura.twod_projectiles.config.ProjectileDirection;
import com.gaura.twod_projectiles.config.TwoDProjectilesConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public final class TwoDProjectiles {

    public static final String MOD_ID = "twod_projectiles";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static TwoDProjectilesConfig CONFIG = new TwoDProjectilesConfig();

    public static void init() {

        AutoConfig.register(TwoDProjectilesConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(TwoDProjectilesConfig.class).getConfig();
    }

    public static float getArrowAngle(ItemStack itemStack) {

        return Arrays.stream(CONFIG.arrowDirectionList)
                .filter(arrowDirection -> itemStack.getItemHolder().is(ResourceLocation.parse(arrowDirection.arrow)))
                .map(arrowDirection -> arrowDirection.direction.getDegree())
                .findFirst()
                .orElse(ProjectileDirection.UP_RIGHT.getDegree());
    }
}
