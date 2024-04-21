package com.aqwsxlostfly.packandgo.packandgo.GameState;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Point2D implements Json.Serializable{
    private float x, y;

    public Point2D(Point2D p){
        x = p.getX();
        y = p.getY();
    }

    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void addCords(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void addVector(Point2D p) {
        this.x += p.getX();
        this.y += p.getY();
    }

    public void setPoint(Point2D p){
        x = p.getX();
        y = p.getY();
    }

    public void setPoint(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Point2D getPoint(){
        return this;
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

    @Override
    public void write(Json json) {
        json.writeValue("x", x);
        json.writeValue("y", y);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }
}

