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

    private final SpriteBatch batch = new SpriteBatch();

    private final OrthographicCamera camera;

    private static PlayScreen gameScreen;

    private HomeSc homeScreen;

    private static Screen currentScreen;

    private BitmapFont font;

    public Renderer() {

        camera = new OrthographicCamera(150, 100);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 13;
        parameter.color = new Color(Color.RED);
        font = generator.generateFont(parameter);
        generator.dispose();

        Renderer.gameScreen = new PlayScreen();


    }

    public Screen getCurrentScreen() {
        return homeScreen;
    }

    public Screen getGameScreen() {
        return gameScreen;
    }

    public void setCurrentScreen(Screen currentScreen) {
        currentScreen = currentScreen;
    }

    public void render() {

        if (currentScreen instanceof HomeSc){
            homeScreen.render(batch, camera);
        }else{
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.setProjectionMatrix(camera.combined); // Устанавливаем матрицу проекции для рендеринга с позиции камеры игры
            batch.begin();

            gameScreen.render(batch, camera);

            batch.end();
        }



    }

    public void setHomeScreen(HomeSc screen) {
        homeScreen = screen;
        currentScreen = homeScreen;
//        homeScreen.screenToChange(newScreen -> homeScreen = (HomeSc) screen);
    }

    public static void setGameScreen() {
        currentScreen = gameScreen;
//        gameScreen.screenToChange(newScreen -> gameScreen = (PlayScreen) gameScreen);
    }

}