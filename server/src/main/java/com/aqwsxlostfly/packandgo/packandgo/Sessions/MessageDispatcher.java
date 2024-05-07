package com.aqwsxlostfly.packandgo.packandgo.Sessions;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

@Slf4j
@Component
@Getter
public class MessageDispatcher {

    @Autowired
    private GameSessionManager gameSessionManager;

    public void processMessage(StandardWebSocketSession session, JsonNode message) {

        String userId = session.getId();
        String type = message.get("type").asText();
        switch (type) {
            case "createRoom" -> {
                String sessionId = message.get("sessionId").asText();
                String sessionPassword = message.get("sessionPassword").asText();
                gameSessionManager.createGameSession(userId, sessionId, sessionPassword, 2, session);
            }
            case "joinRoom" -> {
                String sessionJoinId = message.get("sessionId").asText();
                String sessionJoinPassword = message.get("sessionPassword").asText();
                gameSessionManager.joinGameSession(userId, sessionJoinId, sessionJoinPassword, session);
            }
            case "playerState", "furnitureState" -> gameSessionManager.updateGameSession(userId, message);
            default -> throw new RuntimeException("Unknown WS object type: " + type);
        }

    }


}
