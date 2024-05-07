package com.aqwsxlostfly.packandgo.packandgo.Config;

import com.aqwsxlostfly.packandgo.packandgo.GameLoop;
import com.aqwsxlostfly.packandgo.packandgo.GameStateTools.Player;
import com.aqwsxlostfly.packandgo.packandgo.Sessions.GameSessionToSend;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
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
        json.addClassTag("sessionRoom", GameSessionToSend.class);
        return json;
    }
}
