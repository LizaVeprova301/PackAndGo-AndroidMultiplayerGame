package com.aqwsxlostfly.packandgo.render;

import com.aqwsxlostfly.packandgo.Screens.HomeSc;
import com.aqwsxlostfly.packandgo.Screens.PlayScreen;
import com.aqwsxlostfly.packandgo.Screens.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Renderer {

    public static final SpriteBatch batch = new SpriteBatch();

    private final OrthographicCamera camera;

    private static PlayScreen gameScreen;

    private HomeSc homeScreen;

    private static Screen currentScreen;

    public Renderer() {
        camera = new OrthographicCamera(150, 100);
    }

    public void render() {

        if (currentScreen instanceof HomeSc){
            homeScreen.render(batch, camera);
        }else{
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            gameScreen.render(batch, camera);

            batch.end();
        }

    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public Screen getGameScreen() {
        return gameScreen;
    }

    public Screen getHomeScreen() {
        return homeScreen;
    }

    public static void setCurrentScreen(Screen screen) {
        currentScreen = screen;
    }

    public void setHomeScreen(HomeSc screen) {
        homeScreen = screen;
        setCurrentScreen(homeScreen);
    }

    public static void setGameScreen(PlayScreen screen) {
        gameScreen = screen;
    }

    public static void changeToPlayScreen(){
        currentScreen = gameScreen;
    }

}