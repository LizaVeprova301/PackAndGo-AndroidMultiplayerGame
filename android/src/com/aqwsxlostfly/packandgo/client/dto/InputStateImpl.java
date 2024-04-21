package com.aqwsxlostfly.packandgo.client.dto;


import com.aqwsxlostfly.packandgo.InputState;
import com.aqwsxlostfly.packandgo.Tools.Point2D;

public class InputStateImpl implements InputState {

    private String type;
    private float x;
    private float y;
    private int score;
    private float health;
    private boolean ghost;
    public float speed;
    public float radius;

    public InputStateImpl() {

    }

//    public InputStateImpl(Point2D position, float x, float y, int score, float health, boolean ghost, float speed, float radius) {
//        this();
//        this.position = position;
//        this.x = x;
//        this.y = y;
//        this.score = score;
//        this.health = health;
//        this.ghost = ghost;
//        this.speed = speed;
//        this.radius = radius;
//    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return this.type;
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
    public void setX(float x) {
        this.x =x;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public void setY(float y) {
        this.y =y;
    }

    @Override
    public float getY() {
        return this.y;
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
