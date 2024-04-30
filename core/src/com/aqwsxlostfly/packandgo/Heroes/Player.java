package com.aqwsxlostfly.packandgo.Heroes;

import static com.aqwsxlostfly.packandgo.Tools.TileMapHelper.collisionLayer;
import static com.aqwsxlostfly.packandgo.Tools.TileMapHelper.worldHeight;
import static com.aqwsxlostfly.packandgo.Tools.TileMapHelper.worldWigth;

import com.aqwsxlostfly.packandgo.Tools.TileMapHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.HashMap;
import java.util.Objects;

public class Player extends Heroes {

    Label gameNameLabel;



    private float increment;

    public static boolean inPoint = false;

    Skin skin;

    private MapObject textT;

    private BitmapFont font;
    public String currentCheckpointLetter = "";


    public Player(Body body, TextureMapObject textureMapObject) {

        super(body, textureMapObject);

//        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));


    }

    public void setText(String text){
        gameNameLabel = new Label(text, skin);
    }

    @Override
    public void update() {

//        float oldX = body.getPosition().x;
//        float oldY = body.getPosition().y;

        float newXDir =  direction.getX()*body.getAngularVelocity();
        float newYDir =  direction.getY()*body.getAngularVelocity();

        float newX = getX() + newXDir;
        float newY = getY() + newYDir;

//        float newX = oldX+newXDir;
//        float newY = oldY+newYDir;
//
//        if (newX <= 0){
//            newX = 0;
//        }
//        if (newY <= 0) {
//            newY = 0;
//        }
//        if (newX >= worldWigth) {
//            newX = oldX;
//        }
//        if (newY >= worldHeight) {
//            newY = oldY;
//        }

        Vector2 velocity = new Vector2(newXDir, newYDir);

        // save old position
        float oldX = body.getPosition().x, oldY = body.getPosition().y;
        boolean collisionX = false, collisionY = false;

        // calculate the increment for step in #collidesLeft() and #collidesRight()
        increment = collisionLayer.getTileWidth();
        increment = getWidth() < increment ? getWidth() / 2 : increment / 2;

//        Gdx.app.log("VELOCITY", "vlx " + newXDir + "  vly " + newYDir);

        boolean isBlock = false;

        if (collisionLayer.getCell((int)newX,(int)newY)!= null){
            isBlock = collisionLayer.getCell((int)newX,(int)newY).getTile().getProperties().containsKey("blocked");
        }

//        Gdx.app.log("COLLIDE BLOCK", "playerPosX " + body.getPosition().x + "  vly " + body.getPosition().y
//        + "  " +  isBlock
//        );

        if(velocity.x < 0) // going left
            collisionX = collidesLeft();
        else if(velocity.x > 0) // going right
            collisionX = collidesRight();

        // react to x collision
        if(collisionX) {
            newX = oldX;
            velocity.x = 0;
        }

        // move on y
//        setY(getY() + velocity.y * delta * 5f);

        // calculate the increment for step in #collidesBottom() and #collidesTop()
        increment = collisionLayer.getTileHeight();
        increment = getHeight() < increment ? getHeight() / 2 : increment / 2;

        boolean canJump;
        if(velocity.y < 0) // going down
            canJump = collisionY = collidesBottom();
        else if(velocity.y > 0) // going up
            collisionY = collidesTop();

        // react to y collision
        if(collisionY) {
            newY = oldY;
            velocity.y = 0;
        }


        if (newX <= 0){
            newX = 0;
        }
        if (newY <= 0) {
            newY = 0;
        }
        if (newX >= worldWigth) {
            newX = oldX;
        }
        if (newY >= worldHeight) {
            newY = oldY;
        }
//        Gdx.app.log("VELOCITY", "vlx " + newX + "  vly " + newY);
//        if (newX >= 593) {
//            if ((newY >= 663) && (newY <= 769)) {
//                flag
//                newX = oldX;
//                newY = oldY;
//            }
//        }

        ObjectMap<String, MapObject> hashMap = TileMapHelper.walls;
        inPoint = false; // Чтобы текст обновился только один раз при входе в точку

        for(MapObject taskPoint: hashMap.values()){

//            if (Objects.equals(taskPoint.getName(), "text")){
//                textT = taskPoint;
//            }

            float objXleft = (float) taskPoint.getProperties().get("x");
            float objXright = (float) taskPoint.getProperties().get("x") + (float) taskPoint.getProperties().get("width");

            float objYbottom = (float) taskPoint.getProperties().get("y") + (float) taskPoint.getProperties().get("height");
            float objYtop = (float) taskPoint.getProperties().get("y");


//            Gdx.app.log("CHECK COLLISIN", "pX " + newX + " pY " + newY + "  oX " +
//
//                    objXleft + ' ' + objXright + "   oY " + objYtop + ' ' + objYbottom + "   " + taskPoint.getName());


            if(newX >= objXleft - 10 && newX <= objXright){
                if(newY >= objYtop && newY <= objYbottom){
                    inPoint = true;

//                    checkpoint.getLetter();
                    newX = oldX;
                    newY = oldY;
                }
            }




        }




        body.setTransform(new Vector2(newX, newY), direction.getX()*10f + direction.getY()*10f);

    }

    public void setAngle(float angle){
        body.setTransform(getX(), getY(), angle);
    }

    private boolean isCellBlocked(float x, float y) {
        TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
        String blockedKey = "blocked";
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
    }

    public boolean collidesRight() {
        for(float step = 0; step <= getHeight(); step += increment)
            if(isCellBlocked(getX() + getWidth(), getY() + step))
                return true;
        return false;
    }

    public boolean collidesLeft() {
        for(float step = 0; step <= getHeight(); step += increment)
            if(isCellBlocked(getX(), getY() + step))
                return true;
        return false;
    }

    public boolean collidesTop() {
        for(float step = 0; step <= getWidth(); step += increment)
            if(isCellBlocked(getX() + step, getY() + getHeight()))
                return true;
        return false;

    }

    public boolean collidesBottom() {
        for(float step = 0; step <= getWidth(); step += increment)
            if(isCellBlocked(getX() + step, getY()))
                return true;
        return false;
    }

    public boolean isInPoint() {
        return inPoint;
    }

    public void setInPoint(boolean inPoint) {
        this.inPoint = inPoint;
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
//        batch.begin();
        batch.draw(textureRegion,
                body.getPosition().x, body.getPosition().y,
                textureRegion.getRegionWidth() / 2.0f, textureRegion.getRegionHeight() / 2.0f,
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
                textureMapObject.getScaleX(),textureMapObject.getScaleY(), body.getAngle());

//        batch.end();
//        batch.begin();


//        batch.end();
    }
}


