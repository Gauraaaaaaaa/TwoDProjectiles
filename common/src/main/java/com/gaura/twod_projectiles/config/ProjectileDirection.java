package com.gaura.twod_projectiles.config;

public enum ProjectileDirection {

    UP("Up", -90.0F),
    DOWN("Down", 90.0F),
    LEFT("Left", 180.0F),
    RIGHT("Right", 0.0F),
    UP_RIGHT("Up Right", -45.0F),
    UP_LEFT("Up Left", -135.0f),
    DOWN_RIGHT("Down Right", 45.0F),
    DOWN_LEFT("Down Left", 135.0F);

    private final String direction;
    private final float degree;

    ProjectileDirection(String direction, float degree) {

        this.direction = direction;
        this.degree = degree;
    }

    public float getDegree() {

        return degree;
    }

    @Override
    public String toString() {

        return direction;
    }
}
