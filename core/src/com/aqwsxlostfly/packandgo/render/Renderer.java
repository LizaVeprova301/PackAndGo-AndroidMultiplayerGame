package com.aqwsxlostfly.packandgo.render;

import com.aqwsxlostfly.packandgo.Screens.PlayScreen;
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

    private PlayScreen currentScreen;

    private BitmapFont font;

    public Renderer() {

        camera = new OrthographicCamera(150, 100);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 13;
        parameter.color = new Color(Color.RED);
        font = generator.generateFont(parameter); // Здесь шрифт создаётся и может быть использован повторно
        generator.dispose();


    }

    public PlayScreen getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(PlayScreen currentScreen) {
        this.currentScreen = currentScreen;
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined); // Устанавливаем матрицу проекции для рендеринга с позиции камеры игры
        batch.begin();

//        SpriteBatch spriteBatch = new SpriteBatch();
//        spriteBatch.begin();

//        spriteBatch.end();
        currentScreen.render(batch, camera);

//        batch.setProjectionMatrix(camera.combined); // Устанавливаем матрицу проекции для рендеринга с позиции камеры игры


        batch.end();
    }

    public void setScreen(PlayScreen screen) {
        currentScreen = screen;
//        currentScreen.screenToChange(newScreen -> currentScreen = newScreen);
    }

}
