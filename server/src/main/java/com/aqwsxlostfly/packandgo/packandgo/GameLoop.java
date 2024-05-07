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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class GameLoop extends ApplicationAdapter {
    private final MyWebSocketHandler socketHandler;
    private final Json json;

    private static final float frameRate = 1/50f;
    private float lastRender = 0;

    public static final ExecutorService threadPool =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 2);

    private static final String SESSION_KEY_MESSAGE = "{\"class\":\"sessionKey\",\"id\":\"%s\"}";
    private static final String EVICT_MESSAGE = "{\"class\":\"evict\",\"id\":\"%s\"}";

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
            sendMessage(String.format(SESSION_KEY_MESSAGE, session.getId()), session);
        });
        socketHandler.setDisconnectListener(session -> {

            sendToEverybodyInSession(
                    String.format(EVICT_MESSAGE, session.getId()),
                    gameSessionManager.getAllUsersInSessionByUserId(session.getId())
            );

            gameSessionManager.disconnectGameSession(session.getId());
        });
        socketHandler.setMessageListener(((session, message) -> {
            threadPool.execute(() -> {
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
                threadPool.execute(() -> {
                    processGameSession(gameSession);
                });
            }

            lastRender = 0;
        }
    }


    private void processGameSession(GameSession gameSession) {
        Array<Player> stateToSend = new Array<>();
        for (Player player : gameSession.getGameState().getPlayersObjectMap().values()) {
            player.setId(player.getId());
            player.setY(player.getY());
            player.setX(player.getX());
            stateToSend.add(player);
        }

        String stateJson = json.toJson(stateToSend);
        ArrayList<String> usersInSession = new ArrayList<>(gameSession.getActiveUserSessions().keySet());
        sendToEverybodyInSession(stateJson, usersInSession);
    }

    private void sendToEverybody(String json) {
        threadPool.execute(() -> {
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

    private void sendToEverybodyInSession(String json, ArrayList<String> sessionIdsToSend) {
        threadPool.execute(() -> {
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
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS))
                    log.error("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        super.dispose();
    }
}
