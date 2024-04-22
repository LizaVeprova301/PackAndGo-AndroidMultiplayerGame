package com.aqwsxlostfly.packandgo.packandgo.GameStateTools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Getter
public class GameState {

    // TODO
    // Concurrent map
    private final ObjectMap<String, Player> playersObjectMap = new ObjectMap<>();

//    TODO
//    public final ObjectMap<String, Furniture> furnitureObjectMap = new ObjectMap<>();

    public void updateState(JsonNode state) {
        String type = state.get("type").asText();
        switch (type) {
            case "playerState":
                String playerId = state.get("id").asText();
                float new_x = state.get("x").asLong();
                float new_y = state.get("y").asLong();
                updatePlayer(playerId, new_x, new_y);
                break;
            case "furnitureState":
                // TODO
                break;
        }
    }

    public Player getPlayerById(String playerId) {
        if (this.playersObjectMap.containsKey(playerId)) {
            return this.playersObjectMap.get(playerId);
        } else {
            // TODO
            Gdx.app.log("GAME STATE", "ERROR GETTING PLAYER - PLAYER " + playerId + " DOES NOT EXISTS");
            return null;
        }
    }

    public void addPlayer(String playerId) {
//        TODO
//        check if input playerId is correct
        if (!this.playersObjectMap.containsKey(playerId)) {
            Player player = new Player();
            player.setId(playerId);
            this.playersObjectMap.put(playerId, player);
        } else {
            // TODO
            Gdx.app.log("GAME STATE", "ERROR ADDING PLAYER - PLAYER " + playerId + " EXISTS");
        }
    }

    public void evictPLayer(String playerId) {
//        TODO
//        check if input playerId is correct
        if (this.playersObjectMap.containsKey(playerId)) {
            this.playersObjectMap.remove(playerId);
        } else {
            // TODO
            Gdx.app.log("GAME STATE", "ERROR REMOVING PLAYER - PLAYER " + playerId + " DOES NOT EXISTS");
        }
    }

    public void updatePlayer(String playerId, Float new_x, Float new_y) {
//        TODO
//        check if input playerId is correct
        if (this.playersObjectMap.containsKey(playerId)) {
            this.playersObjectMap.get(playerId).update(new_x, new_y);
        } else {
            // TODO
            Gdx.app.log("GAME STATE", "ERROR UPDATING PLAYER - PLAYER " + playerId + " DOES NOT EXISTS");

        }
    }

    public Integer getPLayersAmount() {
        return this.playersObjectMap.size;
    }

}
