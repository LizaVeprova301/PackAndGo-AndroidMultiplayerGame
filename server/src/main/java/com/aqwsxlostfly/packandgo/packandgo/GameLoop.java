package com.aqwsxlostfly.packandgo.packandgo;

import com.aqwsxlostfly.packandgo.packandgo.GameStateTools.Player;
import com.aqwsxlostfly.packandgo.packandgo.Sessions.GameSession;
import com.aqwsxlostfly.packandgo.packandgo.Sessions.GameSessionManager;
import com.aqwsxlostfly.packandgo.packandgo.Sessions.MessageDispatcher;
import com.aqwsxlostfly.packandgo.packandgo.Ws.MyWebSocketHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

@Slf4j
@Component
public class GameLoop extends ApplicationAdapter {
    private final MyWebSocketHandler socketHandler;
    private final Json json;

    private static final float frameRate = 1 / 60f;
    private float lastRender = 0;
    private final ForkJoinPool pool = ForkJoinPool.commonPool();

    @Autowired
    private GameSessionManager gameSessionManager;

    @Autowired
    private MessageDispatcher messageDispatcher;

    public GameLoop(MyWebSocketHandler socketHandler, Json json) {
        this.socketHandler = socketHandler;
        this.json = json;
    }

    @Override
    public void create() {
        socketHandler.setConnectListener(session -> {
            sendMessage(String.format("{\"class\":\"sessionKey\",\"id\":\"%s\"}", session.getId()), session);
        });
        socketHandler.setDisconnectListener(session -> {

            sendToEverybodyInSession(
                    String.format("{\"class\":\"evict\",\"id\":\"%s\"}", session.getId()),
                    gameSessionManager.getAllUsersInSessionByUserId(session.getId())
            );

            gameSessionManager.disconnectGameSession(session.getId());
        });
        socketHandler.setMessageListener(((session, message) -> {
            pool.execute(() -> {
                messageDispatcher.processMessage(session, message);
            });
        }));

    }


    @Override
    public void render() {
        float currentTime = Gdx.graphics.getDeltaTime();
        lastRender += currentTime;

        if (lastRender >= frameRate) {
            ObjectMap<String, GameSession> sessionsCopy = new ObjectMap<>(gameSessionManager.getActiveGameSessions());

            for (ObjectMap.Entry<String, GameSession> gameSessionEntry : sessionsCopy.entries()) {
                GameSession gameSession = gameSessionEntry.value;
                pool.execute(() -> {
                    Array<Player> stateToSend = new Array<>();
                    for (ObjectMap.Entry<String, Player> playerEntry : gameSession.getGameState().getPlayersObjectMap()) {
                        Player player = playerEntry.value;
                        player.setId(player.getId());
                        player.setY(player.getY());
                        player.setX(player.getX());
                        stateToSend.add(player);
                    }

                    String stateJson = json.toJson(stateToSend);
                    ArrayList<String> usersInSession = new ArrayList<>(gameSession.getActiveUserSessions().keySet());
                    sendToEverybodyInSession(stateJson, usersInSession);
                });
            }

            lastRender = 0;
        }
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

    private void sendToEverybodyInSession(String json, ArrayList<String> sessionIdsToSend) {
        pool.execute(() -> {
            Array<StandardWebSocketSession> sessionsCopy = new Array<>(socketHandler.getSessions());
            for (StandardWebSocketSession session : sessionsCopy) {
                try {
                    if (session.isOpen() && sessionIdsToSend.contains(session.getId())) {
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
