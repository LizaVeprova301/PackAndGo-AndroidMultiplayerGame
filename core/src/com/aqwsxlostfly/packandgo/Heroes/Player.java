package com.aqwsxlostfly.packandgo.Heroes;

import static com.aqwsxlostfly.packandgo.Tools.TileMapHelper.worldHeight;
import static com.aqwsxlostfly.packandgo.Tools.TileMapHelper.worldWigth;

import com.aqwsxlostfly.packandgo.Tools.Point2D;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Player extends Heroes {

    private static final float VELOCITY_SCALE = 70.0f;

    public Player(Body body, TextureMapObject textureMapObject) {
        super(body, textureMapObject);
    }

    public void setDirection(Point2D direction) {
        this.direction = direction;
        updateVelocity();
    }

    private void updateVelocity() {
        // Задаем линейную скорость в соответствии с направлением.
        // Если есть стена, Box2D автоматически обработает коллизию и остановит движение в этом направлении.
        Vector2 newDir = new Vector2(direction.getX()*VELOCITY_SCALE, direction.getY()*VELOCITY_SCALE);
        body.setLinearVelocity(newDir);
    }

    @Override
    public void update() {

    }

    public void testUpdate(float newX, float newY){
        body.setTransform(new Vector2(newX, newY), body.getLinearVelocity().x*10f + body.getLinearVelocity().y*10f);
    }

    public void setAngle(float angle){
        body.setTransform(getX(), getY(), angle);
    }

    public float getX(){
        return body.getPosition().x;
    }

    public float getY(){
        return body.getPosition().y;
    }

    public void setTransform(float x, float y, float angle){

        body.setTransform(x, y, angle);
    }


    public float getWidth(){
        return worldWigth;
    }

    public float getHeight(){
        return worldHeight;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(textureRegion,
                body.getPosition().x-textureRegion.getRegionWidth()/ 2.0f, body.getPosition().y-textureRegion.getRegionHeight() / 2.0f,
                textureRegion.getRegionWidth() / 2.0f, textureRegion.getRegionHeight() / 2.0f,
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
                textureMapObject.getScaleX(),textureMapObject.getScaleY(), 0);
    }
}


