package com.aqwsxlostfly.packandgo.packandgo.Sessions;

import com.aqwsxlostfly.packandgo.packandgo.GameStateTools.GameState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.aqwsxlostfly.packandgo.packandgo.GameLoop.threadPool;


@Slf4j
@Component
@Getter
public class GameSessionManager {

    private final ConcurrentHashMap<String, String> userToSession = new ConcurrentHashMap<>();

    private final ObjectMap<String, GameSession> activeGameSessions = new ObjectMap<>();

//    private final ForkJoinPool pool = ForkJoinPool.commonPool();

    private final Json json;

    public GameSessionManager(Json json) {
        this.json = json;
    }

    public String getSessionIdByUserId(String userSessionId) {
        // TODO
        return userToSession.getOrDefault(userSessionId, null);
    }

    public void updateGameSession(String userId, JsonNode state) {
        String sessionId = getSessionIdByUserId(userId);
//        Gdx.app.log("updateGameSession", "userId " + userId + " sessionId " + sessionId + " state " + state);


        if (activeGameSessions.containsKey(sessionId)) {
            activeGameSessions.get(sessionId).updateGameState(state);
        } else {
            // TODO
            Gdx.app.log("UPDATE SESSION", "ERROR UPDATE SESSION - SESSION " + userId
                    + " DOES NOT EXISTS");
        }
    }

    public void joinGameSession(String userId, String sessionId, String sessionPassword, StandardWebSocketSession session) {
        if (!activeGameSessions.containsKey(sessionId)) {
            Gdx.app.log("JOIN SESSION", "ERROR JOINING SESSION - SESSION "
                    + sessionId + " DOES NOT EXISTS");
            Array<GameSessionToSend> stateToSend = new Array<>();
            GameSessionToSend gameSessionToSend = new GameSessionToSend(sessionId, sessionPassword, "does_not_exists");
            stateToSend.add(gameSessionToSend);

            String stateJson = json.toJson(stateToSend);

            sendMessage(stateJson, session);
            return;
        }
        if (!userToSession.containsKey(userId)) {
            Gdx.app.log("JOIN SESSION", "userId " + userId + " sesPsw " + sessionPassword
                    + " sesActualPsw " + activeGameSessions.get(sessionId).getSessionPassword());
            if (Objects.equals(sessionPassword, activeGameSessions.get(sessionId).getSessionPassword())) {
                Gdx.app.log("JOIN SESSION AFTER PSW", "userId " + userId );
                userToSession.put(userId, sessionId);
                activeGameSessions.get(sessionId).addUserSession(userId);
            } else {
                // TODO
                Gdx.app.log("JOIN SESSION", "ERROR JOINING SESSION - USER "
                        + userId + " INCORRECT PASSWORD " + sessionPassword);
                Array<GameSessionToSend> stateToSend = new Array<>();
                GameSessionToSend gameSessionToSend = new GameSessionToSend(sessionId, sessionPassword, "incorrect_password");
                stateToSend.add(gameSessionToSend);

                String stateJson = json.toJson(stateToSend);

                sendMessage(stateJson, session);
            }
        } else {
            // TODO
            Gdx.app.log("JOIN SESSION", "ERROR JOINING SESSION - USER " + userId + " EXISTS IN SESSION");
        }
    }


    public void createGameSession(String userId, String sessionId, String sessionPassword, Integer userLimit, StandardWebSocketSession session) {
        if (!userToSession.containsKey(userId)) {
            GameState gameState = new GameState();
            GameSession gameSession = new GameSession(sessionId, sessionPassword, userLimit, gameState);
            gameSession.addUserSession(userId);

            activeGameSessions.put(sessionId, gameSession);
            userToSession.put(userId, sessionId);

            Gdx.app.log("CREATED GAMESESSION", gameSession.toString());

            Array<GameSessionToSend> stateToSend = new Array<>();
            GameSessionToSend gameSessionToSend = new GameSessionToSend(sessionId, sessionPassword, "connected_ok");
            stateToSend.add(gameSessionToSend);

            String stateJson = json.toJson(stateToSend);

            sendMessage(stateJson, session);
        } else {
            // TODO
            Array<GameSessionToSend> stateToSend = new Array<>();
            GameSessionToSend gameSessionToSend = new GameSessionToSend(sessionId, sessionPassword, "session_exists");
            stateToSend.add(gameSessionToSend);

            String stateJson = json.toJson(stateToSend);

            sendMessage(stateJson, session);
            Gdx.app.log("ADD SESSION", "ERROR ADDING SESSION - SESSION " + sessionId + " EXISTS");
        }
    }

    private void sendMessage(String json, StandardWebSocketSession session) {
        threadPool.execute(() -> {
            try {
                if (session.isOpen()) {
                    session.getNativeSession().getBasicRemote().sendText(json);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    public void disconnectGameSession(String userId) {
        String sessionId = getSessionIdByUserId(userId);
        if (activeGameSessions.containsKey(sessionId)) {
            if (activeGameSessions.get(sessionId).getGameState().getPLayersAmount() == 1) {
                activeGameSessions.get(sessionId).removeUserSession(userId);
                activeGameSessions.remove(sessionId);
                userToSession.remove(userId);
            } else {
                activeGameSessions.get(sessionId).removeUserSession(userId);
            }
        } else {
            // TODO
            Gdx.app.log("DISCONNECT SESSION", "ERROR DISCONNECT SESSION - SESSION " + userId
                    + " DOES NOT EXISTS");
        }
    }

    public ArrayList<String> getAllUsersInSessionByUserId(String userId) {
        String sessionId = getSessionIdByUserId(userId);
        GameSession session = activeGameSessions.get(sessionId);

        if (session == null) {
            return new ArrayList<>();
        }

        Map<String, String> usersInSession = session.getActiveUserSessions();

        return new ArrayList<>(usersInSession.keySet());
    }


}
