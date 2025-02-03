package com.gaura.twod_projectiles.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "twod_projectiles")
public class TwoDProjectilesConfig implements ConfigData {

    @ConfigEntry.Category("bow_and_crossbow")
    public float arrow_scale = 1.5F;
    @ConfigEntry.Category("bow_and_crossbow")
    public float arrow_stuck_in_player_scale = 1.5F;
    @ConfigEntry.Category("bow_and_crossbow")
    public float arrow_shake_factor = 50.0F;
    @ConfigEntry.Category("bow_and_crossbow")
    public boolean render_critical_particles = false;

    @ConfigEntry.Category("bow")
    public float bow_x_translation = -0.15F;
    @ConfigEntry.Category("bow")
    public float bow_y_translation = -0.2F;
    @ConfigEntry.Category("bow")
    public float bow_z_translation = 0.0F;
    @ConfigEntry.Category("bow")
    public float bow_y_rotation = -90.0F;
    @ConfigEntry.Category("bow")
    public float bow_z_rotation = -45.0F;

    @ConfigEntry.Category("crossbow")
    public boolean flat_arrow_with_crossbow = true;
    @ConfigEntry.Category("crossbow")
    public float crossbow_x_translation = -0.125F;
    @ConfigEntry.Category("crossbow")
    public float crossbow_y_translation = -0.25F;
    @ConfigEntry.Category("crossbow")
    public float crossbow_z_translation = 0.05F;
    @ConfigEntry.Category("crossbow")
    public float crossbow_y_rotation = -180.0F;
    @ConfigEntry.Category("crossbow")
    public float crossbow_x_rotation = -90.0F;
    @ConfigEntry.Category("crossbow")
    public float crossbow_z_rotation = 45.0F;

    @ConfigEntry.Category("firework_rocket")
    public float firework_rocket_scale = 1.5F;
    @ConfigEntry.Category("firework_rocket")
    public float firework_rocket_x_translation = 0.0F;
    @ConfigEntry.Category("firework_rocket")
    public float firework_rocket_y_translation = 0.0F;
    @ConfigEntry.Category("firework_rocket")
    public float firework_rocket_z_translation = 0.0F;
    @ConfigEntry.Category("firework_rocket")
    public float firework_rocket_y_rotation = 180.0F;
    @ConfigEntry.Category("firework_rocket")
    public float firework_rocket_x_rotation = 0.0F;
}