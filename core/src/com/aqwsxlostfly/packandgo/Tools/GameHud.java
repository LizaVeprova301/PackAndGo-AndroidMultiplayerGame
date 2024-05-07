//package com.aqwsxlostfly.packandgo.Tools;
//
//
//import static com.aqwsxlostfly.packandgo.AssetManagerSingleton.getAssetManager;
//
//import com.aqwsxlostfly.packandgo.Heroes.Player;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//
//public class GameHud {
//    Texture tabouret;
//    Texture hero;
//    public Stage stage; // Используется для управления и отображения UI
//    public Skin skin; // Скин для стилизации элементов UI
//    public TextButton takeBtn; // Кнопка в UI
//    public TextButton fightBtn; // Кнопка в UI
//    public Player player;
//
//
//    public GameHud(Player player) {
//        tabouret = new Texture("tabouret.png");
//        hero = new Texture("tile_0456.png");
//        // Инициализация скина и сцены
//        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
//        stage = new Stage(new ScreenViewport());
//
//        // Установка обработчика ввода в текущую сцену
//        //Gdx.input.setInputProcessor(stage);
//
//        // Создание и настройка кнопки
//        takeBtn = new TextButton("Take", skin);
//        fightBtn = new TextButton("GIVE PUSSY", skin);
//        takeBtn.setSize(250, 150); // Установка размера кнопки
//        takeBtn.setPosition(Gdx.graphics.getWidth() -450, Gdx.graphics.getHeight() / 2 - 450); // Позиционирование кнопки
//        fightBtn.setSize(250, 150); // Установка размера кнопки
//        fightBtn.setPosition(Gdx.graphics.getWidth() -450, Gdx.graphics.getHeight() / 2 - 250); // Позиционирование кнопки
//
//        this.player = player;
//
//        // Добавление кнопки на сцену
//        stage.addActor(takeBtn);
//        stage.addActor(fightBtn);
//
//        // Добавление обработчика событий для кнопки
//        addListeners();
//
//    }
//
//    public void draw() {
//        // Отрисовка сцены (и всех элементов UI на ней)
//        stage.draw();
//    }
//
//    public void update(float delta) {
//        // Обновление сцены
//        stage.act(delta);
//    }
//
//    private void addListeners() {
//        takeBtn.addListener(new ClickListener() {
//
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                Texture currentTexture = player.textureMapObject.getTextureRegion().getTexture();
//                // Обработка нажатия на кнопку
//                Gdx.app.log("Button Clicked", "Take button was pressed");
//                if (currentTexture != tabouret){
//                    Gdx.app.log("INFO","смена текстуры");
//                    player.textureMapObject.getTextureRegion().setTexture(tabouret);
//
//                } else{
//                    //player.textureMapObject.getTextureRegion().setTexture(null);
//                    player.textureMapObject.getTextureRegion().setTexture(hero);
//                }
//            }
//        });
//    }
//
//    // Вызовите этот метод в вашем основном цикле приложения для обеспечения обновления UI
//    public void resize(int width, int height) {
//        stage.getViewport().update(width, height, true);
//    }
//
//    // Не забудьте освободить ресурсы
//    public void dispose() {
//        stage.dispose();
//        skin.dispose();
//    }
//}