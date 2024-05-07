package com.aqwsxlostfly.packandgo.packandgo.GameStateTools;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Player implements Json.Serializable {
    private String id;
    private float x;
    private float y;

    public void update(Float new_x, Float new_y) {
        this.x = new_x;
        this.y = new_y;
    }

    @Override
    public void write(Json json) {
        json.writeValue("id", id);
        json.writeValue("x", x);
        json.writeValue("y", y);
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

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
