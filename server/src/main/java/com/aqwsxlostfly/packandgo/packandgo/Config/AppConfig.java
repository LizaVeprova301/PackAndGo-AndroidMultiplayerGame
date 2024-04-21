package com.aqwsxlostfly.packandgo.packandgo.Config;

import com.aqwsxlostfly.packandgo.packandgo.GameLoop;
import com.aqwsxlostfly.packandgo.packandgo.GameSession;
import com.aqwsxlostfly.packandgo.packandgo.GameState.Player;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public HeadlessApplication getApplication(GameLoop gameLoop) {
        return new HeadlessApplication(gameLoop);
    }

    @Bean
    public Json getJson() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("player", Player.class);
//        json.addClassTag("mysession", GameSession.class);
        return json;
    }
}
