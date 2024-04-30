package com.aqwsxlostfly.packandgo.Tools;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class BodyHelperService {

    public static Body createBody(TextureMapObject textureObject, World world, boolean isStatic) {
        TextureRegion textureRegion = textureObject.getTextureRegion();

        float width = textureRegion.getRegionWidth();
        float height = textureRegion.getRegionHeight();

        float x = textureObject.getX() + width / 2;
        float y = textureObject.getY() + height / 2;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / 32.0f, y / 32.0f);
        bodyDef.angularVelocity = 4f;
        bodyDef.fixedRotation = true;

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / 32.0f, height / 2 / 32.0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        shape.dispose();

        return body;
    }
}