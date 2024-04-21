package com.aqwsxlostfly.packandgo.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

public class FlyingObject extends Actor {
    private TextureRegion textureRegion;
    private float speed;

    public FlyingObject(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        this.setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        this.speed = MathUtils.random(100.0f, 200.0f); // Рандомная скорость в пределах [100, 200]
        resetPosition();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.moveBy(0, -speed * delta); // Двигаемся вниз
        if (this.getY() + this.getHeight() < 0) {
            resetPosition();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    private void resetPosition() {
        float newX = MathUtils.random(0, Gdx.graphics.getWidth() - getWidth());
        float newY = Gdx.graphics.getHeight();
        setPosition(newX, newY);
    }
}

