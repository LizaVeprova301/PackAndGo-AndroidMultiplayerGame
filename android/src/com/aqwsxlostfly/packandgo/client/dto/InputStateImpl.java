package com.aqwsxlostfly.packandgo.client.dto;


import com.aqwsxlostfly.packandgo.InputState;
import com.aqwsxlostfly.packandgo.Tools.Point2D;

public class InputStateImpl implements InputState {

    private Point2D position;
    private int score;
    private float health;
    private boolean ghost;
    public float speed;
    public float radius;

    @Override
    public void setPosition(Point2D position) {
        this.position = position;
    }

    @Override
    public Point2D getPosition() {
        return this.position;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public void setHealth(float health) {
        this.health = health;
    }

    @Override
    public float getHealth() {
        return this.health;
    }

    @Override
    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public float getRadius() {
        return this.radius;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public boolean getGhost() {
        return ghost;
    }

    @Override
    public void setGhost(boolean ghost) {
        this.ghost = ghost;
    }
}
