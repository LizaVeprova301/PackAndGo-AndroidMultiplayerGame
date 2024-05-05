package com.aqwsxlostfly.packandgo.Heroes;

import com.aqwsxlostfly.packandgo.Tools.Point2D;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com. badlogic.gdx.physics.box2d.Body;

public abstract class Heroes {
    public Point2D direction;
    protected Body body;
    public TextureMapObject textureMapObject;
    public TextureRegion textureRegion;

    public Heroes(Body body, TextureMapObject textureMapObject) {
        this.body = body;
        this.direction = new Point2D(0, 0);
        this.textureMapObject = textureMapObject;
        this.textureRegion = textureMapObject.getTextureRegion();
    }

    public abstract void draw(SpriteBatch batch);
    public Body getBody() {
        return body;
    }
    public void setDirection(Point2D p){
        direction = p;
    }

}
