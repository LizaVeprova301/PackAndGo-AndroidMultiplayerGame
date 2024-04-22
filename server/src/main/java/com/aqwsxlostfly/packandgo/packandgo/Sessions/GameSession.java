package com.aqwsxlostfly.packandgo.packandgo.Sessions;

import com.aqwsxlostfly.packandgo.packandgo.GameStateTools.GameState;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@ToString
public class GameSession {
    private final String sessionId;
    private final String sessionPassword;
    private final Integer userLimit;

    private final Map<String, String> activeUserSessions = new HashMap<>();


    private GameState gameState;

    public GameSession(String sessionId, String sessionPassword, Integer userLimit, GameState gameState) {
        this.sessionId = sessionId;
        this.sessionPassword = sessionPassword;
        this.userLimit = userLimit;
        this.gameState = gameState;
    }

    public void addUserSession(String userid) {
        this.activeUserSessions.put(userid, userid);
        gameState.addPlayer(userid);
    }

    public void removeUserSession(String userid) {
        this.activeUserSessions.remove(userid);
        gameState.evictPLayer(userid);
    }

    public void updateGameState(JsonNode state) {
        this.gameState.updateState(state);
    }

}

