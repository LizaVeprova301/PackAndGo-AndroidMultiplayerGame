package com.aqwsxlostfly.packandgo;



import com.aqwsxlostfly.packandgo.Screens.PlayScreen;
import com.aqwsxlostfly.packandgo.Tools.Point2D;
import com.aqwsxlostfly.packandgo.render.Renderer;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Objects;


public class Main extends Game {

    public Renderer renderer;

    public GameSession gameSession;
    public static String meId = "666";
    public InputState inputState;
    public MessageSender messageSender;

    public Main(InputState inputState) {

        this.inputState = inputState;

        this.gameSession = new GameSession(false);
    }



    public static int screenWidth;
    public static int screenHeight;
    public static int record;

    public boolean socketState;

//	public WaitingSc waitingSc;


    @Override
    public void create() {


//        batch = new SpriteBatch();
//        img = new Texture("packlogo.png");
//        circle = new Texture("circle.png");

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
//		this.waitingSc = new WaitingSc(this);
        renderer = new Renderer();
        renderer.setScreen(new PlayScreen());
//        setScreen(new WaitingSc(this));
    }

    @Override
    public void render() {
        renderer.render();
    }



    public void evict(String idToEvict) {
        PlayScreen.players.remove(idToEvict);
    }

//	public void handleTimer() {
//		this.waitingSc.handleTimer();
//	}


    @Override
    public void dispose() {

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



}
