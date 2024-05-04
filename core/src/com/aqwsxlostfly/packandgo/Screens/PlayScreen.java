package com.aqwsxlostfly.packandgo.Screens;


import static com.aqwsxlostfly.packandgo.Main.meId;
import static com.aqwsxlostfly.packandgo.Main.messageSender;
import static com.aqwsxlostfly.packandgo.Main.screenHeight;
import static com.aqwsxlostfly.packandgo.Main.screenWidth;

import com.aqwsxlostfly.packandgo.Heroes.Player;
import com.aqwsxlostfly.packandgo.InputState;
import com.aqwsxlostfly.packandgo.Main;
import com.aqwsxlostfly.packandgo.Tools.GameHud;
import com.aqwsxlostfly.packandgo.Tools.Joystick;
import com.aqwsxlostfly.packandgo.Tools.Point2D;
import com.aqwsxlostfly.packandgo.Tools.TileMapHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Timer;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PlayScreen implements Screen {

    Joystick joy;
    private final TileMapHelper tileMapHelper;

    private static final float frameRate = 1/60f;
    public GameHud gameHud;

//    public final Player player;
    private Player line;
    public static ObjectMap<String, Player> players = new ObjectMap<>();

    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private final OrthographicCamera hudCamera;



    public static int count;


    public PlayScreen() {
        hudCamera = new OrthographicCamera(screenWidth, screenHeight);
        hudCamera.viewportWidth = screenWidth; // Размеры для камеры HUD
        hudCamera.viewportHeight = screenHeight;
        hudCamera.position.set(screenWidth / 2, screenHeight / 2, 0);
        hudCamera.update();


        this.tileMapHelper = new TileMapHelper();
        orthogonalTiledMapRenderer = tileMapHelper.setupMap();

        updatePlayerArray(meId, 0,0);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        gameHud = new GameHud(players.get(meId)); // (или передайте player если он нужен для инициализации GameHud)
        inputMultiplexer.addProcessor(gameHud.stage); // Первый обработчик, чтобы убедиться, что UI имеет приоритет
        inputMultiplexer.addProcessor(new InputAdapter() { // Ваш текущий обработчик событий в PlayScreen
            // Здесь ваш обработчик событий...

            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                screenY = screenHeight - screenY;
                multitouch((int) screenX, (int) screenY, true, pointer);
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                screenY = screenHeight - screenY;
                multitouch((int) screenX, (int) screenY, false, pointer);
                return false;
            }

            @Override
            public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
                return touchUp(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return touchDown(screenX, screenY, pointer, 0);
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return false;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);

        loadHeroes();


    }

    public void updatePlayerArray(String id, float x_, float y_) {

        Gdx.app.log("updatePlayerArray", "id " + id + " x " + x_ + " y " + y_ );

//        Player player = players.get(id);
        if (players.get(id) == null) {
            Gdx.app.log("ADD NEW PLAYER", "id " + id + " x " + x_ + " y " + y_ );
//            Main.players.put(id, new Player(Main.capibara, new Point2D(Main.screenWidth / 2, Main.screenHeight / 2), 10F, (float) (Main.screenHeight / 5), 5));
            if (Objects.equals(id, meId)){
                players.put(id, tileMapHelper.getPlayer());
            }else{
                players.put(id, tileMapHelper.getOtherPlayer());
            }
        } else if (!Objects.equals(id, meId)){
            Gdx.app.log("UPDATE GAME STATE ARRAY", "NEW MSG SERVER STATE" + "new x " + x_ + " new y " + y_ );
//            player.setDirection(new Point2D(x_, y_));
//            player.setScoreValue(score);
//            player.setHealth(health);
//            player.setGhost(ghost);
            players.get(id).testUpdate(x_, y_);
        }
    }





//    public Player getPlayer(){
//        return player;
//    }

    public void multitouch(float x, float y, boolean isDownTouch, int pointer) {
        for (int i = 0; i < 3; i++) {
            joy.update(x, y, isDownTouch, pointer);
        }
    }

    private int isActive = 0;

    public void render(SpriteBatch batch, OrthographicCamera camera) {

        updatePlayers();

        cameraUpdate(camera);

        orthogonalTiledMapRenderer.setView(camera); // Устанавливаем камеру для рендерера карты
        orthogonalTiledMapRenderer.render(); // Рендерим карту

//        for (String key : players.keys()) {
//            players.get(key).draw(batch);
//        }

        Array<String> keys = new Array<>(players.keys().toArray());
        for (String key : keys) {
            players.get(key).draw(batch);
        }


        // Начинаем рисовать элементы интерфейса с использованием камеры HUD
        batch.setProjectionMatrix(hudCamera.combined);

        joy.draw(batch); // Рисуем джойстик с использованием камеры HUD
        gameHud.update(Math.min(1 / 30F, 1 / 30f));
        gameHud.draw();
    }

    public void updatePlayers(){
        players.get(meId).setDirection(joy.getDir());
        players.get(meId).update();
    }



    @Override
    public void dispose() {
        gameHud.dispose();
    }

    public void loadHeroes() {

        joy = new Joystick(new Texture("circle.png"),
                new Texture("circle.png"),
                new Point2D(((screenHeight / 3) / 2 + (screenHeight / 3) / 4),
                        (screenHeight / 3) / 2 + (screenHeight / 3) / 4), screenHeight / 3);

        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                handleTimer();
            }
        }, 0, frameRate);
    }

    private void cameraUpdate(OrthographicCamera camera) {
        // Получаем размеры карты
        MapProperties properties = tileMapHelper.tiledMap.getProperties();
        int mapWidth = properties.get("width", Integer.class);
        int mapHeight = properties.get("height", Integer.class);
        int tilePixelWidth = properties.get("tilewidth", Integer.class);
        int tilePixelHeight = properties.get("tileheight", Integer.class);
        int mapPixelWidth = mapWidth * tilePixelWidth;
        int mapPixelHeight = mapHeight * tilePixelHeight;

        // Устанавливаем позицию камеры на игрока
        camera.position.set(players.get(meId).getBody().getPosition().x, players.get(meId).getBody().getPosition().y, 0);

        // Ограничиваем движение камеры границами карты
        float cameraHalfWidth = camera.viewportWidth * 0.5f;
        float cameraHalfHeight = camera.viewportHeight * 0.5f;

        // Ограничиваем, что камера не выходит за пределы карты
        float cameraLeft = clamp(camera.position.x - cameraHalfWidth, 0, mapPixelWidth - camera.viewportWidth);
        float cameraBottom = clamp(camera.position.y - cameraHalfHeight, 0, mapPixelHeight - camera.viewportHeight);

        camera.position.x = cameraLeft + cameraHalfWidth;
        camera.position.y = cameraBottom + cameraHalfHeight;

        camera.update();
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
    public void handleTimer() {
        if (!players.isEmpty()) {
            Player me = players.get(meId);
            InputState playerState = updateAndGetInputState(me);
            Gdx.app.log("SEND MESSAGE", "HANDLE TIMER, send state");
            messageSender.sendMessage(playerState);
        }
    }

    public InputState updateAndGetInputState(Player player) {
        InputState inputState = Main.inputState;

        inputState.setType("playerState");
        inputState.setId(meId);
        inputState.setX(player.getX());
        inputState.setY(player.getY());

        return inputState;
    }
}

