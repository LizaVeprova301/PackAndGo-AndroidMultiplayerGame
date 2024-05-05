package com.aqwsxlostfly.packandgo.client;

import com.aqwsxlostfly.packandgo.Main;
import com.aqwsxlostfly.packandgo.client.ws.WsEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;


public class MessageProcessor {
    private final Main main;

    public MessageProcessor(Main main) {
        this.main = main;
    }

    public void processEvent(WsEvent event) {
        String data = event.getData();

        if (data != null) {

            JsonReader jsonReader = new JsonReader();
            JsonValue parsed = jsonReader.parse(data);

            if (parsed.isArray()) {
                processArray(parsed);
            } else if (parsed.isObject()) {
                processObject(parsed);
            }
        }
    }


    private void processArray(JsonValue array) {
        for (JsonValue value : array) {
            if (value.isObject()) {
                processObject(value);
            }
        }
    }

    private void processObject(JsonValue object) {
        String type = object.getString("class", null);
        if (type != null) {
            switch (type) {
                case "sessionKey":
                    String meId = object.getString("id");
                    main.setMeId(meId);
                    break;
                case "sessionRoom":
                    String sessionId = object.getString("id");
                    String sessionPassword = object.getString("password");
                    String sessionMsg = object.getString("msg");
                    main.gameSession.setConnected(true);
                    main.gameSession.setId(sessionId);
                    main.gameSession.setPassword(sessionPassword);
                    main.gameSession.setSessionMsg(sessionMsg);
                    break;
                case "evict":
                    String idToEvict = object.getString("id");
                    main.evict(idToEvict);
                    break;
                case "player":

                    String id = object.getString("id");
                    float x = object.getFloat("x");
                    float y = object.getFloat("y");

                    main.renderer.getGameScreen().updatePlayerArray(id, x, y);
                    break;
                default:
                    throw new RuntimeException("Unknown message type " + type);
            }
        }
    }

}