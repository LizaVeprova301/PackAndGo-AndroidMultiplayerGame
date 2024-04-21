package com.aqwsxlostfly.packandgo.packandgo;

import com.aqwsxlostfly.packandgo.packandgo.GameState.GameState;
import com.aqwsxlostfly.packandgo.packandgo.GameState.Player;
import com.aqwsxlostfly.packandgo.packandgo.GameState.Point2D;
import com.aqwsxlostfly.packandgo.packandgo.Ws.MyWebSocketHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;


@Component
public class GameLoop extends ApplicationAdapter {
    private static final float frameRate = 1 / 60f;
    private final MyWebSocketHandler socketHandler;

    private final Json json;
    private float lastRender = 0;

    private final ForkJoinPool pool = ForkJoinPool.commonPool();
    private final Array<String> events = new Array<>();

    private final ObjectMap<String, GameSession> gameSessions = new ObjectMap<>();
    private final Map<String, String> userToSession = new HashMap<>();

    private final Array<Player> stateToSend = new Array<>();
    private GameState gameState = new GameState();

    private static final String JOIN_MSG = "Just joined";
    private static final String LEFT_MSG = "Just left";
    private static final String SAID_MSG = " said ";

    public GameLoop(MyWebSocketHandler socketHandler, Json json) {
        this.socketHandler = socketHandler;
        this.json = json;
    }

    @Override
    public void create() {

        socketHandler.setConnectListener(session -> {

//            GameSession gameSession = new GameSession(session.getId(), session.getId(), true);
//
//            Player player = new Player();
//            player.setId(session.getId());
//            userToSession.put(session.getId(), gameSession.getId());
//            gameSession.players.put(session.getId(), player);
//
//            gameSessions.put(gameSession.getId(), gameSession);

            try {
                session
                        .getNativeSession()
                        .getBasicRemote()
                        .sendText(
                                String.format("{\"class\":\"sessionKey\",\"id\":\"%s\"}", session.getId())
                        );
            } catch (IOException e) {
                e.printStackTrace();
            }

//            events.add(session.getId() + JOIN_MSG);
//            if(!gameState.containPlayer(session.getId())){
//                gameState.addPlayer(session.getId());
//                gameState.updateAmount();
//                events.add("playersAmount " + gameState.getPlayersAmount());
//            }
        });
        socketHandler.setDisconnectListener(session -> {
            String sessionId = userToSession.get(session.getId());
            sendToEverybodyInSession(
                    String.format("{\"class\":\"evict\",\"id\":\"%s\"}", session.getId()),
                    sessionId
            );
            gameSessions.get(sessionId).players.remove(session.getId());

//            events.add(session.getId() + LEFT_MSG);
//            if(gameState.containPlayer(session.getId())){
//                gameState.removePlayer(session.getId());
//                gameState.updateAmount();
//                events.add("playersAmount " + gameState.getPlayersAmount());
//            }
        });
        socketHandler.setMessageListener(((session, message) -> {

            pool.execute(() -> {
//                String sessionId = userToSession.get(session.getId());
                String type = message.get("type").asText();
                switch (type) {
                    case "create_room":
                        String id = message.get("sessionId").asText();
                        String createPassword = message.get("sessionPassword").asText();
                        GameSessionImpl sendSessionCreate = new GameSessionImpl("mysession", id, createPassword, true);
                        if(!gameSessions.containsKey(id)) {

                            GameSession gameSession = new GameSession(id, createPassword, true);

                            Player player = new Player();
                            player.setId(session.getId());
                            userToSession.put(session.getId(), gameSession.getId());
                            gameSession.players.put(session.getId(), player);

                            gameSession.setConnected(true);
                            gameSession.setPlayersAmount(1);
                            gameSession.setSessionErrorMsg("connect_ok");
                            gameSessions.put(gameSession.getId(), gameSession);

                            sendSessionCreate.setSessionErrorMsg("connect_ok");
                            sendMessage( json.toJson(sendSessionCreate), session);

                            Gdx.app.log("CREATED_SESSION", gameSession.toString());
                        } else {
                            sendSessionCreate.setSessionErrorMsg("id_exist");
                            sendMessage( json.toJson(sendSessionCreate), session);
                        }
                        break;
                    case "join_room":
                        String joinId = message.get("sessionId").asText();
                        String joinPassword = message.get("sessionPassword").asText();

                        GameSessionImpl sendSessionJoin = new GameSessionImpl("mysession", joinId, joinPassword, true);

                        if(gameSessions.containsKey(joinId)) {
                            GameSession gameSession = gameSessions.get(joinId);
                            Gdx.app.log("JOINING TO", gameSession.toString());
                            if (Objects.equals(joinPassword, gameSession.getPassword())){
                                Player player = new Player();
                                player.setId(session.getId());
                                userToSession.put(session.getId(), gameSession.getId());
                                gameSessions.get(joinId).players.put(session.getId(), player);
                                gameSessions.get(joinId).setPlayersAmount(gameSessions.get(joinId).players.size);
                                sendSessionJoin.setPlayersAmount(gameSessions.get(joinId).players.size);
                                sendSessionJoin.setSessionErrorMsg("joined_ok");
                                sendMessage( json.toJson(sendSessionJoin), session);
                            }else {
                                sendSessionJoin.setSessionErrorMsg("incorrect_password");
                                sendMessage( json.toJson(sendSessionJoin), session);
                            }
                            break;
                        } else {
                            sendSessionJoin.setSessionErrorMsg("id_does_not_exist");
                            sendMessage( json.toJson(sendSessionJoin), session);
                        }
                        break;
                    case "state":
                        String sessionId = userToSession.get(session.getId());
                        Player player = gameSessions.get(sessionId).players.get(session.getId());
                        player.setPosition(new Point2D(message.get("x").asLong() / frameRate, message.get("y").asLong() / frameRate));
                        player.setX(message.get("x").asLong() / frameRate);
                        player.setY(message.get("y").asLong() / frameRate);
                        player.setGhost(message.get("ghost").asBoolean());
                        player.setHealth(message.get("health").asLong());
                        player.setRadius(message.get("radius").asLong());
                        player.setScore(message.get("score").asInt());
                        player.setSpeed(message.get("speed").asLong());
                        break;
                    default:
                        throw new RuntimeException("Unknown WS object type: " + type);
                }
            });

//            String playerInfo = "UPDATE" + session + " " + message;
//            System.out.println(message);
//            events.add(session.getId() + SAID_MSG + message);
//            events.add("playersAmount " + gameState.getPlayersAmount());
        }));

    }


    @Override
    public void render() {

        for(ObjectMap.Entry<String, GameSession> gameSession : gameSessions) {
            String sessionId = gameSession.value.getId();

            lastRender += Gdx.graphics.getDeltaTime();
            if (lastRender >= frameRate) {
                stateToSend.clear();
                for (ObjectMap.Entry<String, Player> playerEntry : gameSessions.get(sessionId).players) {
                    Player player = playerEntry.value;
                    player.setY(player.getY() / frameRate);
                    player.setX(player.getX() / frameRate);
//                player.act(lastRender);
                    stateToSend.add(player);
                }

                lastRender = 0;
                String stateJson = json.toJson(stateToSend);

                sendToEverybodyInSession(stateJson, sessionId);
            }

        }

//        for (WebSocketSession session : socketHandler.getSessions()) {
//            try {
//                for (String event : events) {
//                    session.sendMessage(new TextMessage(event));
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        events.clear();

    }

    private void sendToEverybody(String json) {
        pool.execute(() -> {
            Array<StandardWebSocketSession> sessionsCopy = new Array<>(socketHandler.getSessions());
            for (StandardWebSocketSession session : sessionsCopy) {
                try {
                    if (session.isOpen()) {
                        session.getNativeSession().getBasicRemote().sendText(json);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendMessage(String json, StandardWebSocketSession session) {
        pool.execute(() -> {
            try {
                if (session.isOpen()) {
                    session.getNativeSession().getBasicRemote().sendText(json);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private void sendToEverybodyInSession(String json, String sessionId) {
        pool.execute(() -> {
            Array<StandardWebSocketSession> sessionsCopy = new Array<>(socketHandler.getSessions());
            for (StandardWebSocketSession session : sessionsCopy) {
                try {
                    if (session.isOpen() && Objects.equals(userToSession.get(session.getId()), sessionId)) {
                        session.getNativeSession().getBasicRemote().sendText(json);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
