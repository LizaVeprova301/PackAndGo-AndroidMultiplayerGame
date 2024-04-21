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
        Gdx.app.log("PROCESSED EVENT", data);
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
        String classtype = object.getString("classtype", null);
        if (type == null){
            type = classtype;
        }
        if (type != null) {
            switch (type) {
                case "sessionKey":
                    String meId = object.getString("id");
                    main.setMeId(meId);
                    break;
                case "mysession":
                    String sesid = object.getString("sesid");
                    String password = object.getString("password");
                    String playersAmount = object.getString("playersAmount");
                    String sessionErrorMsg = object.getString("sessionErrorMsg");
                    main.gameSession.setConnected(true);
                    main.gameSession.setId(sesid);
                    main.gameSession.setPassword(password);
                    main.gameSession.setPlayersAmount(Integer.valueOf(playersAmount));
                    main.gameSession.setSessionErrorMsg(sessionErrorMsg);
                    break;
                case "evict":
                    String idToEvict = object.getString("id");
                    main.evict(idToEvict);
                    break;
                case "player":
                    String id = object.getString("id");
                    float x = object.getFloat("x");
                    float y = object.getFloat("y");
                    int score = object.getInt("score");
                    float health = object.getFloat("health");
                    float speed = object.getFloat("speed");
                    float radius = object.getFloat("radius");
                    boolean ghost = object.getBoolean("ghost");
                    main.updatePlayerArray(id, x, y, score, health, speed, radius, ghost);
                    break;
                default:
                    throw new RuntimeException("Unknown message type " + type);
            }
        }
    }

}