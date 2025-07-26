package com.gaura.twod_projectiles.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "twod_projectiles")
public class TwoDProjectilesConfig implements ConfigData {

    @ConfigEntry.Category("arrow")
    public boolean renderTwoDArrow = true;
    @ConfigEntry.Category("arrow")
    public ArrowDirection[] arrowDirectionList = {
            new ArrowDirection("minecraft:arrow", ProjectileDirection.UP_RIGHT),
            new ArrowDirection("minecraft:tipped_arrow", ProjectileDirection.UP_RIGHT),
            new ArrowDirection("minecraft:spectral_arrow", ProjectileDirection.UP_RIGHT),
    };
    @ConfigEntry.Category("arrow")
    public float arrowScale = 1.5F;
    @ConfigEntry.Category("arrow")
    public float arrowOffset = 0.15F;
    @ConfigEntry.Category("arrow")
    public float arrowRoll = 22.5F;
    @ConfigEntry.Category("arrow")
    public float arrowShakePowerFactor = 200.0F;
    @ConfigEntry.Category("arrow")
    public float arrowShakeSpeedFactor = 2.0F;
    @ConfigEntry.Category("arrow")
    public boolean renderTippedArrow = false;
    @ConfigEntry.Category("arrow")
    public boolean renderCriticalParticles = false;

    @ConfigEntry.Category("fireworkRocket")
    public boolean renderTwoDFireworkRocket = true;
    @ConfigEntry.Category("fireworkRocket")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ProjectileDirection fireworkRocketDirection = ProjectileDirection.UP;
    @ConfigEntry.Category("fireworkRocket")
    public float fireworkRocketScale = 1.5F;
    @ConfigEntry.Category("fireworkRocket")
    public float fireworkRocketOffset = 0.125F;
    @ConfigEntry.Category("fireworkRocket")
    public float fireworkRocketRoll = 22.5F;

    @ConfigEntry.Category("trident")
    public boolean renderTwoDTrident = false;
    @ConfigEntry.Category("trident")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ProjectileDirection tridentDirection = ProjectileDirection.UP_RIGHT;
    @ConfigEntry.Category("trident")
    public float tridentScale = 1.5F;
    @ConfigEntry.Category("trident")
    public float tridentOffset = 0.125F;
    @ConfigEntry.Category("trident")
    public float tridentRoll = 22.5F;
    @ConfigEntry.Category("trident")
    public float tridentShakePowerFactor = 100.0F;
    @ConfigEntry.Category("trident")
    public float tridentShakeSpeedFactor = 2.0F;

    @ConfigEntry.Category("thrownItem")
    public boolean renderTwoDThrownItem = true;
    @ConfigEntry.Category("thrownItem")
    public float thrownItemScale = 1.5F;
}
