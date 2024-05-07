package com.aqwsxlostfly.packandgo.Screens;


import static com.aqwsxlostfly.packandgo.Main.meId;
import static com.aqwsxlostfly.packandgo.Main.messageSender;
import static com.aqwsxlostfly.packandgo.Main.screenHeight;
import static com.aqwsxlostfly.packandgo.Main.screenWidth;
import static com.aqwsxlostfly.packandgo.Tools.TileMapHelper.world;

import com.aqwsxlostfly.packandgo.Heroes.Player;
import com.aqwsxlostfly.packandgo.InputState;
import com.aqwsxlostfly.packandgo.Main;
import com.aqwsxlostfly.packandgo.Tools.Joystick;
import com.aqwsxlostfly.packandgo.Tools.Point2D;
import com.aqwsxlostfly.packandgo.Tools.TileMapHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Timer;

import java.util.Objects;

public class PlayScreen implements Screen {

    Joystick joy;
    private final TileMapHelper tileMapHelper;

    private static final float frameRateRender = 1/60f;

    private static final float frameRateTimer = 1/50f;

    private final int mapPixelWidth;
    private final int mapPixelHeight;

    public static ObjectMap<String, Player> players = new ObjectMap<>();

    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private final OrthographicCamera hudCamera;
    private final Box2DDebugRenderer box2DDebugRenderer = new Box2DDebugRenderer();


    public PlayScreen() {
        hudCamera = new OrthographicCamera(screenWidth, screenHeight);
        hudCamera.viewportWidth = screenWidth; // Размеры для камеры HUD
        hudCamera.viewportHeight = screenHeight;
        hudCamera.position.set((float) screenWidth / 2, (float) screenHeight / 2, 0);
        hudCamera.update();

        this.tileMapHelper = new TileMapHelper();
        orthogonalTiledMapRenderer = tileMapHelper.setupMap();

        updatePlayerArray(meId, 0,0);

        Gdx.input.setInputProcessor(new InputProcessor() {
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


        MapProperties properties = tileMapHelper.tiledMap.getProperties();
        int mapWidth = properties.get("width", Integer.class);
        int mapHeight = properties.get("height", Integer.class);
        int tilePixelWidth = properties.get("tilewidth", Integer.class);
        int tilePixelHeight = properties.get("tileheight", Integer.class);
        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;

        loadHeroes();
    }

    public void updatePlayerArray(String id, float x_, float y_) {

        if (Objects.equals(id, meId)) {
            if (players.get(id) == null) {
                Gdx.app.log("ADD NEW PLAYER", "id " + id + " x " + x_ + " y " + y_);
                players.put(id, tileMapHelper.getPlayer());
            }
        } else {

            if (players.get(id) != null) {
                players.get(id).serverUpdate(x_, y_);
            }else{
                Gdx.app.log("ADD NEW PLAYER", "id " + id + " x " + x_ + " y " + y_);
                players.put(id, tileMapHelper.getNewPlayer());
            }

        }

    }


    public void multitouch(float x, float y, boolean isDownTouch, int pointer) {
        for (int i = 0; i < 3; i++) {
            joy.update(x, y, isDownTouch, pointer);
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {

        updatePlayers();

        cameraUpdate(camera);

        world.step(frameRateRender, 8, 3);

        orthogonalTiledMapRenderer.setView(camera); // Устанавливаем камеру для рендерера карты
        orthogonalTiledMapRenderer.render(); // Рендерим карту

        for (ObjectMap.Entry<String, Player> entry : players.entries()) {
            entry.value.draw(batch);
        }


        // Начинаем рисовать элементы интерфейса с использованием камеры HUD
        batch.setProjectionMatrix(hudCamera.combined);

        joy.draw(batch); // Рисуем джойстик с использованием камеры HUD

        box2DDebugRenderer.render(world,camera.combined.scl(1));

    }

    public void updatePlayers(){
        players.get(meId).setDirection(joy.getDir());
    }



    @Override
    public void dispose() {

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
        }, 0, frameRateTimer);
    }



    private void cameraUpdate(OrthographicCamera camera) {
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
            messageSender.sendMessage(playerState);
        }
    }

    public InputState updateAndGetInputState(Player player) {
        InputState inputState = Main.inputState;

        inputState.setType("playerState");
        inputState.setId(meId);
        inputState.setX(player.getX());
        inputState.setY(player.getY());
//        inputState.setX(player.getBody().getLinearVelocity().x);
//        inputState.setY(player.getBody().getLinearVelocity().y);

        return inputState;
    }
}
