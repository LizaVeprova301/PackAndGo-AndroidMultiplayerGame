package com.aqwsxlostfly.packandgo.Tools;


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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Objects;

public class TileMapHelper {
    public TiledMap tiledMap;
    public static TiledMapTileLayer collisionLayer;
    public static int worldHeight;
    public static int worldWigth;
    private Player player;
    private Player line;
    private final World world;

    public TileMapHelper() {
        this.world = new World(new Vector2(0, 0), false);
    }

    public static ObjectMap<String, MapObject> walls = new ObjectMap<>();


    public OrthogonalTiledMapRenderer setupMap() {
        this.tiledMap = new TmxMapLoader().load("Map.tmx");
        MapProperties properties = tiledMap.getProperties();
        worldHeight = properties.get("height", Integer.class) * 16 - 16;
        worldWigth = properties.get("width", Integer.class) * 16 - 16;
        parseMapObjects(tiledMap.getLayers().get("hero").getObjects());


        collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);


        for(MapLayer mapLayer : tiledMap.getLayers()){
//           Gdx.app.log("CHECK LAYER", "id " + mapLayer.getName());
           if (Objects.equals(mapLayer.getName(), "ground")){

               for(MapObject mapObject   : mapLayer.getObjects()){
                   Gdx.app.log("CHECK OBJECT COUNT CLASS", "id " + mapObject.getName());
                   if (mapObject.getName()!=null && Objects.equals(mapObject.getName(), "wall")) {
                      walls.put( String.valueOf(mapObject.getProperties().get("id")), mapObject);

//                       Gdx.app.log("CHECK OBJECT", "getX " + mapObject.getProperties().get("x"));
                   }
               }

           }
        }




        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    private void parseMapObjects(MapObjects mapObjects) {
        for (MapObject mapObject : mapObjects) {

            TextureMapObject textureMapObject = ((TextureMapObject) mapObject);
            String textureMapObjectName = textureMapObject.getName();

            if (textureMapObjectName != null && textureMapObjectName.equals("hero")) {

                Body body = BodyHelperService.createBody(textureMapObject,
                        world, false);

                body.setTransform(textureMapObject.getX(), textureMapObject.getY(), 0);

                player = new Player(body, textureMapObject);
            } else if (textureMapObjectName != null && textureMapObjectName.equals("tabouret")) {

                Body body = BodyHelperService.createBody(textureMapObject,
                        world, false);

                textureMapObject.setScaleX(1.5f);
                textureMapObject.setScaleY(0.5f);

//                tabouret = new Player(body, textureMapObject);

            }

        }
    }

    public Player getPlayer() {
        return player;
    }

    private Shape createRectangleShape(RectangleMapObject rectangleMapObject) {
        PolygonShape shape = new PolygonShape();
        Vector2 size = new Vector2((rectangleMapObject.getRectangle().width / 2) / 32.0f, (rectangleMapObject.getRectangle().height / 2) / 32.0f);
        shape.setAsBox(size.x, size.y, rectangleMapObject.getRectangle().getCenter(new Vector2()).scl(1 / 32.0f), 0.0f);
        return shape;
    }

    public Player getLine() {
        return line;
    }
}
