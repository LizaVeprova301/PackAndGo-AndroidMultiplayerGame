package com.aqwsxlostfly.packandgo;

import com.aqwsxlostfly.packandgo.Tools.Circle;
import com.aqwsxlostfly.packandgo.Tools.Point2D;

public interface InputState {
    void setPosition(Point2D pos);
    Point2D getPosition();

    void setScore(int score);
    int getScore();

    void setHealth(float health);
    float getHealth();

    void setGhost(boolean isGhost);
    boolean getGhost();

    void setRadius(float radius);
    float getRadius();
}
