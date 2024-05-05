package com.aqwsxlostfly.packandgo.Tools;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class BodyHelperService {

    public static final short CATEGORY_PLAYER = 0x0001;

    public static final short CATEGORY_WALL = 0x0002;

    public static Body createBody(TextureMapObject textureObject, World world, boolean isStatic) {
        TextureRegion textureRegion = textureObject.getTextureRegion();

        float width = textureRegion.getRegionWidth();
        float height = textureRegion.getRegionHeight();

        float x = textureObject.getX() + width /2 ;
        float y = textureObject.getY() + height / 2;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / 16.0f, y / 16.0f);
        bodyDef.angularVelocity = 0f;
        bodyDef.fixedRotation = true;

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 , height / 2 );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0F;
        fixtureDef.friction = 0.3f;
        fixtureDef.filter.categoryBits = CATEGORY_PLAYER;
        fixtureDef.filter.maskBits = CATEGORY_WALL;
        body.createFixture(fixtureDef);

        shape.dispose();


        return body;
    }
}