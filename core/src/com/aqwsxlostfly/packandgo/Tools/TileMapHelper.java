package com.aqwsxlostfly.packandgo.Tools;


import static com.aqwsxlostfly.packandgo.Tools.BodyHelperService.CATEGORY_PLAYER;
import static com.aqwsxlostfly.packandgo.Tools.BodyHelperService.CATEGORY_WALL;

import com.aqwsxlostfly.packandgo.Heroes.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Objects;

public class TileMapHelper {
    public TiledMap tiledMap;
    public static int worldHeight;
    public static int worldWigth;
    private Player player;

    private TextureMapObject textureMapObject;

    public static World world;

    public TileMapHelper() {

        TileMapHelper.world = new World(new Vector2(0, 0), false);

        TileMapHelper.world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                Gdx.app.log("Contact", "beginContact between " + fixtureA.getBody().getType()+ " and " + fixtureB.getBody().getUserData());
            }

            @Override
            public void endContact(Contact contact) {
                // Обработка окончания контакта между двумя фикстурами.
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    public OrthogonalTiledMapRenderer setupMap() {
        this.tiledMap = new TmxMapLoader().load("Map.tmx");
        MapProperties properties = tiledMap.getProperties();
        worldHeight = properties.get("height", Integer.class) * 16;
        worldWigth = properties.get("width", Integer.class) * 16;
        parseMapObjects(tiledMap.getLayers().get("hero").getObjects());



        for(MapLayer mapLayer : tiledMap.getLayers()){
//           Gdx.app.log("CHECK LAYER", "id " + mapLayer.getName());
           if (Objects.equals(mapLayer.getName(), "ground")){

               MapObjects wallsObjs = new MapObjects();

               for(MapObject mapObject   : mapLayer.getObjects()){

                   if (mapObject.getName()!=null && Objects.equals(mapObject.getName(), "wall")) {
                        wallsObjs.add(mapObject);
                   }
               }

               createWalls(wallsObjs);
               createMapBounds(worldWigth, worldHeight);

           }
        }




        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    private void parseMapObjects(MapObjects mapObjects) {
        for (MapObject mapObject : mapObjects) {

            TextureMapObject textureMapObject = ((TextureMapObject) mapObject);
            this.textureMapObject = textureMapObject;
            String textureMapObjectName = textureMapObject.getName();

            if (textureMapObjectName != null && textureMapObjectName.equals("hero")) {


                Body body = BodyHelperService.createBody(textureMapObject,
                        world, false);

                body.setTransform(textureMapObject.getX(), textureMapObject.getY(), 0);

                player = new Player(body, textureMapObject);


            }

        }
    }


    private void createWalls(MapObjects mapObjects) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0; // Стены не имеют веса.
        fixtureDef.friction = 0.3f; // Небольшое трение.
        fixtureDef.filter.categoryBits = CATEGORY_WALL; // Вместо CATEGORY_PLAYER для стен
        fixtureDef.filter.maskBits = CATEGORY_PLAYER;

        for (MapObject mapObject : mapObjects) {
            // Получаем прямоугольник стены из объекта карты.
            RectangleMapObject rectangleObject = (RectangleMapObject) mapObject;
            Rectangle rect = rectangleObject.getRectangle();

            // Set body position to the center of the rectangle.
            bodyDef.position.set(
                    (rect.x + rect.width * 0.5f) ,
                    (rect.y + rect.height * 0.5f)
            );

            // Create the body from the definition.
            Body body = world.createBody(bodyDef);

            // Set the shape as a box using the rectangle's dimensions.
            shape.setAsBox(rect.width * 0.5f  , rect.height * 0.5f );

            // Add the fixture to the body.
            body.createFixture(fixtureDef);
        }

        // Clean up after ourselves. Calling dispose is important to avoid memory leaks!
        shape.dispose();
    }

    private void createMapBounds(int mapWidth, int mapHeight) {
        float halfWidth = mapWidth / 2.0f;
        float halfHeight = mapHeight / 2.0f;
        createWall(halfWidth, -1, mapWidth, 1); // Нижняя граница
        createWall(halfWidth, mapHeight, mapWidth, 1); // Верхняя граница
        createWall(-1, halfHeight, 1, mapHeight); // Левая граница
        createWall(mapWidth, halfHeight, 1, mapHeight); // Правая граница
    }

    private void createWall(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x , y );

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 , height / 2 );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0; // Стены не имеют веса.
        fixtureDef.friction = 0.3f;
        fixtureDef.filter.categoryBits = CATEGORY_WALL; // Вместо CATEGORY_PLAYER для стен
        fixtureDef.filter.maskBits = CATEGORY_PLAYER;
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    public Player getPlayer() {

        Gdx.app.log("ADD NEW PLAYER", "GET NEW PLAYER IN TILE MAP MEE");

        return player;
    }

    public Player getNewPlayer() {

        Gdx.app.log("ADD NEW PLAYER", "GET NEW PLAYER IN TILE NEWWWWWWWWWWWW");

        TextureMapObject textureMapObjectNew = new TextureMapObject(textureMapObject.getTextureRegion());

        Body body = BodyHelperService.createBody(textureMapObjectNew,
                world, false);

        body.setTransform(textureMapObjectNew.getX(), textureMapObjectNew.getY(), 0);

        player = new Player(body, textureMapObjectNew);

        return player;
    }



}
