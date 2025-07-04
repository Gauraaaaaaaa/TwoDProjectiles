package com.gaura.twod_projectiles.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "twod_projectiles")
public class TwoDProjectilesConfig implements ConfigData {

    @ConfigEntry.Category("arrow")
    public ArrowDirection[] arrow_direction_list = {
            new ArrowDirection("minecraft:arrow", ProjectileDirection.UP_RIGHT),
            new ArrowDirection("minecraft:tipped_arrow", ProjectileDirection.UP_RIGHT),
            new ArrowDirection("minecraft:spectral_arrow", ProjectileDirection.UP_RIGHT),
    };
    @ConfigEntry.Category("arrow")
    public float arrow_scale = 1.5F;
    @ConfigEntry.Category("arrow")
    public float arrow_offset = 0.15F;
    @ConfigEntry.Category("arrow")
    public float arrow_roll = 45.0F;
    @ConfigEntry.Category("arrow")
    public float arrow_shake_power_factor = 200.0F;
    @ConfigEntry.Category("arrow")
    public float arrow_shake_speed_factor = 2.0F;
    @ConfigEntry.Category("arrow")
    public boolean render_tipped_arrow = false;
    @ConfigEntry.Category("arrow")
    public boolean render_critical_particles = false;

    @ConfigEntry.Category("firework_rocket")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ProjectileDirection firework_rocket_sprite_direction = ProjectileDirection.UP;
    @ConfigEntry.Category("firework_rocket")
    public float firework_rocket_scale = 1.5F;
    @ConfigEntry.Category("firework_rocket")
    public float firework_rocket_offset = 0.125F;
    @ConfigEntry.Category("firework_rocket")
    public float firework_rocket_roll = 45.0F;
}
