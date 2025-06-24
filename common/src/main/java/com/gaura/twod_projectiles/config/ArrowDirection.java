package com.gaura.twod_projectiles.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ArrowDirection {

    public String arrow;
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ProjectileDirection direction = ProjectileDirection.UP_RIGHT;

    public ArrowDirection() {}

    public ArrowDirection(String arrow, ProjectileDirection direction) {

        this.arrow = arrow;
        this.direction = direction;
    }
}
