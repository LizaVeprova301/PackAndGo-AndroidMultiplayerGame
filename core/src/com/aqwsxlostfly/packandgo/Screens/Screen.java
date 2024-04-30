package com.aqwsxlostfly.packandgo.Screens;

import com.aqwsxlostfly.packandgo.Heroes.Player;
import com.aqwsxlostfly.packandgo.Tools.ScreenConsumer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Screen {


    void render(SpriteBatch batch, OrthographicCamera camera);

    void dispose();

    default void screenToChange(ScreenConsumer screenConsumer) {

    }

    default Player getPlayer(Player player){
        return player;
    }
}
