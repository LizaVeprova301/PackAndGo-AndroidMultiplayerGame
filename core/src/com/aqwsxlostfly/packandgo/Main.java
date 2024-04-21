package com.aqwsxlostfly.packandgo;


import com.aqwsxlostfly.packandgo.Heroes.Player;
import com.aqwsxlostfly.packandgo.Screens.HomeSc;
import com.aqwsxlostfly.packandgo.Screens.WaitingSc;
import com.aqwsxlostfly.packandgo.Tools.Point2D;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Objects;


public class Main extends Game {

    public GameSession gameSession;
    public static String meId;
    public InputState inputState;
    public MessageSender messageSender;

    public static ObjectMap<String, Player> players = new ObjectMap<>();

    public Main(InputState inputState) {

        this.inputState = inputState;

        this.gameSession = new GameSession(false);
    }

    public static SpriteBatch batch;
    public static Texture img;
    public static Texture circle;
    public static Texture capibara;
    public static Texture bullet;
    public static Texture enemy;
    public static Texture ghost;
    public static int screenWidth;
    public static int screenHeight;
    public static int record;

    public boolean socketState;

//	public WaitingSc waitingSc;


    @Override
    public void create() {


        batch = new SpriteBatch();
        img = new Texture("packlogo.png");
        circle = new Texture("circle.png");
        capibara = new Texture("capibara.png");
        bullet = new Texture("burgerBullet.png");
        enemy = new Texture("enemy.png");
        ghost = new Texture("ghost.png");
        if (!Gdx.files.local("rec.txt").exists()) {
            write("0");
        }
        record = read();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
//		this.waitingSc = new WaitingSc(this);
        setScreen(new HomeSc(this));
//        setScreen(new WaitingSc(this));
    }


    public void evict(String idToEvict) {
        Main.players.remove(idToEvict);
    }

//	public void handleTimer() {
//		this.waitingSc.handleTimer();
//	}


    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

    public static void write(String str) {
        FileHandle file = Gdx.files.local("rec.txt");
        file.writeString(str, false);
    }

    public static int read() {
        FileHandle file = Gdx.files.local("rec.txt");
        return Integer.parseInt(file.readString());
    }

    public String getMeId() {
        return meId;
    }

    public void setMeId(String meId) {
        Main.meId = meId;
    }

    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public boolean getSocketState() {
        return socketState;
    }

    public void setSocketState(boolean socketState) {
        this.socketState = socketState;
    }


    public void updatePlayerArray(String id, float x_, float y_, int score, float health, float speed, float radius, boolean ghost) {
        Player player = Main.players.get(id);
        if (player == null) {
            Main.players.put(id, new Player(Main.capibara, new Point2D(Main.screenWidth / 2, Main.screenHeight / 2), 10F, (float) (Main.screenHeight / 5), 5));
        } else if (!Objects.equals(id, meId)){
            Gdx.app.log("UPDATE GAME STATE ARRAY", "NEW MSG SERVER STATE" + "new x " + x_ + " new y " + y_ );
            player.setDirection(new Point2D(x_, y_));
//            player.setScoreValue(score);
//            player.setHealth(health);
//            player.setGhost(ghost);
            player.update();
        }
    }

}
