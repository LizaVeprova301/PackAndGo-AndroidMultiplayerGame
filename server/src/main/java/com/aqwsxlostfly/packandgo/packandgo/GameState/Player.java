package com.aqwsxlostfly.packandgo.packandgo.GameState;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Player implements Json.Serializable {
    private String id;

    private Point2D position;
    private int score;
    private float health;
    private boolean ghost;
    public float speed;
    public float radius;


    @Override
    public void write(Json json) {
        json.writeValue("position", position);
        json.writeValue("score", score);
        json.writeValue("health", health);
        json.writeValue("ghost", ghost);
        json.writeValue("speed", speed);
        json.writeValue("radius", radius);
        json.writeValue("id", id);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public boolean isGhost() {
        return ghost;
    }

    public void setGhost(boolean ghost) {
        this.ghost = ghost;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
