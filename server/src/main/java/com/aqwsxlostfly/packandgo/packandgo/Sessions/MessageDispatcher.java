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
        Gdx.app.log("PROCESS MESSAGE", "Message into processMsg " + message);
        String userId = session.getId();
        String type = message.get("type").asText();
        switch (type) {
            case "createRoom":

                String sessionId = message.get("sessionId").asText();
                String sessionPassword = message.get("sessionPassword").asText();
                gameSessionManager.createGameSession(userId, sessionId, sessionPassword, 2, session);
                Gdx.app.log("Create ROOM", "sesID " + sessionId + " sesPass "+ sessionPassword +
                        "  playersSizez " + gameSessionManager.getActiveGameSessions().get(sessionId).getGameState().getPlayersObjectMap().size);
                break;
            case "joinRoom":


                String sessionJoinId = message.get("sessionId").asText();
                String sessionJoinPassword = message.get("sessionPassword").asText();
                Gdx.app.log("JOIN SESSION processMessage", "sessionJoinId " + sessionJoinId + " sesPsw" + sessionJoinPassword );

                gameSessionManager.joinGameSession(userId, sessionJoinId, sessionJoinPassword);

                Gdx.app.log("JOIN ROOM", "sesID " + sessionJoinId + " sesPass "+ sessionJoinPassword +
                        "  playersSizez " + gameSessionManager.getActiveGameSessions().get(sessionJoinId).getGameState().getPlayersObjectMap().size);
                break;
            case "playerState":
            case "furnitureState":
                gameSessionManager.updateGameSession(userId, message);
                break;
            default:
                throw new RuntimeException("Unknown WS object type: " + type);
        }

    }


}
