package com.aqwsxlostfly.packandgo.client.dto;


import com.aqwsxlostfly.packandgo.InputState;

public class InputStateImpl implements InputState {

    private String type;

    private String id;
    private float x;
    private float y;

    public InputStateImpl() {

    }


    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }


    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public float getY() {
        return this.y;
    }

}
