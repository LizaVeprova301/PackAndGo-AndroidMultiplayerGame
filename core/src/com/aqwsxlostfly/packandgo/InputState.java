package com.aqwsxlostfly.packandgo;


public interface InputState {

    void setType(String type);
    String getType();

    void setScore(int score);
    int getScore();

    void setHealth(float health);
    float getHealth();

    void setGhost(boolean isGhost);
    boolean getGhost();

    void setRadius(float radius);
    float getRadius();

    void setSpeed(float speed);
    float getSpeed();

    void setX(float x);
    float getX();

    void setY(float y);
    float getY();
}
