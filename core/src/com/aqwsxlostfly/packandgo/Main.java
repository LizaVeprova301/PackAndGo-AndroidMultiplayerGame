package com.aqwsxlostfly.packandgo;


import com.aqwsxlostfly.packandgo.Screens.HomeSc;
import com.aqwsxlostfly.packandgo.Screens.PlayScreen;
import com.aqwsxlostfly.packandgo.render.Renderer;
import com.aqwsxlostfly.packandgo.session.GameSession;
import com.aqwsxlostfly.packandgo.session.InputState;
import com.aqwsxlostfly.packandgo.session.MessageSender;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;


public class Main extends Game {

    public Renderer renderer;

    public GameSession gameSession;
    public static String meId = "666";
    public static InputState inputState;
    public static MessageSender messageSender;

    public Main(InputState inputState) {

        Main.inputState = inputState;

        this.gameSession = new GameSession(false);
    }


    public static int screenWidth;
    public static int screenHeight;

    public boolean socketState;


    @Override
    public void create() {

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        renderer = new Renderer();
        renderer.setHomeScreen(new HomeSc(this));
    }

    @Override
    public void render() {
        renderer.render();
    }


    public void evict(String idToEvict) {
        PlayScreen.players.remove(idToEvict);
    }


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