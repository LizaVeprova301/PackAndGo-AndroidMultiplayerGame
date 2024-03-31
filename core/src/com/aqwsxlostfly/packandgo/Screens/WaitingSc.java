package com.aqwsxlostfly.packandgo.Screens;

import com.aqwsxlostfly.packandgo.Heroes.Bullet;
import com.aqwsxlostfly.packandgo.Heroes.Player;
import com.aqwsxlostfly.packandgo.InputState;
import com.aqwsxlostfly.packandgo.Main;
import com.aqwsxlostfly.packandgo.Tools.BulletGenerator;
import com.aqwsxlostfly.packandgo.Tools.Joystick;
import com.aqwsxlostfly.packandgo.Tools.Point2D;
import com.aqwsxlostfly.packandgo.TouchProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.ArrayList;

public class WaitingSc implements Screen {

    private TouchProcessor inputProcessor;
    Main main;

    public static ArrayList<Bullet> bullets;

    public static ObjectMap<String, Player> players = new ObjectMap<>();


    BitmapFont font;
    GlyphLayout textWaiting, textJoin, textErrorWsConnection;
    public static BulletGenerator bulletGenerator;

    public WaitingSc(Main main){
        this.main = main;
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator((Gdx.files.internal(("font.ttf"))));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = Main.screenWidth / 30;
        p.color = new Color(Color.RED);
        font = gen.generateFont(p);
        textWaiting = new GlyphLayout();
        textWaiting.setText(font, "WAITING OTHER PLAYER");
        textJoin = new GlyphLayout();
        textJoin.setText(font, "PLAYER HAS JOINED");
        textErrorWsConnection = new GlyphLayout();
        textErrorWsConnection.setText(font, "NETWORK CONNECTION FAILED!");
        this.inputProcessor = new TouchProcessor(main.inputState);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputProcessor);
        loadHeroes();
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameUpdate();
        Main.batch.begin();
        gameRender(Main.batch);
        Main.batch.end();
    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {}

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Main.batch.dispose();
        for (Player value : players.values()) {
            value.dispose();
        }
    }

    public void loadHeroes() {
        Player me = new Player(Main.capibara, new Point2D(Main.screenWidth / 2, Main.screenHeight / 2), 10F, (float) (Main.screenHeight / 5), 5);
        players.put(main.getMeId(), me);
        bullets = new ArrayList<Bullet>();
        bulletGenerator = new BulletGenerator();
    }


    public void gameUpdate() {

        Player player = players.get(main.getMeId());

        if (player == null) {
            String id = "666";
            player = new Player(Main.capibara, new Point2D(Main.screenWidth / 2, Main.screenHeight / 2), 10F, (float) (Main.screenHeight / 5), 5);
            players.put(id, player);
        } else {
            player.setDirection(inputProcessor.joy.getDir());
            player.update();
        }

        bulletGenerator.update(inputProcessor.joyBullet);
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update();
            if (bullets.get(i).isOut) {
                bullets.remove(i--);
            }
        }
    }

    public void gameRender(SpriteBatch batch) {
        if (!main.getSocketState()){
            font.draw(batch, textErrorWsConnection, Main.screenWidth / 2 - textErrorWsConnection.width / 2, Main.screenHeight -Main.screenHeight/10);
        } else if (players.size < 2) {
            font.draw(batch, textWaiting, Main.screenWidth / 2 - textWaiting.width / 2, Main.screenHeight -Main.screenHeight/10);
        } else {
            font.draw(batch, textJoin, Main.screenWidth / 2 - textWaiting.width / 2, Main.screenHeight -Main.screenHeight/10 );
        }
        for (String key : players.keys()) {
            players.get(key).draw(batch);
        }
        inputProcessor.joy.draw(batch);
        inputProcessor.joyBullet.draw(batch);
        for (int i = bullets.size() - 1; i >= 0; i--) {
            bullets.get(i).draw(batch);
        }
    }

    public void handleTimer() {
        if (inputProcessor != null && !players.isEmpty()) {
            Player me = players.get(main.getMeId());
            InputState playerState = inputProcessor.updateAndGetInputState(me.position);
            main.messageSender.sendMessage(playerState);
        }
    }

    public void evict(String idToEvict) {
        players.remove(idToEvict);
    }


}
