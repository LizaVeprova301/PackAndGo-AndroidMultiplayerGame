package com.aqwsxlostfly.packandgo.packandgo;

import com.aqwsxlostfly.packandgo.packandgo.GameState.GameState;
import com.aqwsxlostfly.packandgo.packandgo.GameState.Player;
import com.aqwsxlostfly.packandgo.packandgo.Ws.MyWebSocketHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


import java.io.IOException;
import java.util.concurrent.ForkJoinPool;


@Component
public class GameLoop extends ApplicationAdapter {
    private static final float frameRate = 1 / 60f;
    private final MyWebSocketHandler socketHandler;

    private final Json json;
    private float lastRender = 0;
    private final ObjectMap<String, Player> players = new ObjectMap<>();
    private final Array<Player> stateToSend = new Array<>();

    private final ForkJoinPool pool = ForkJoinPool.commonPool();
    private final Array<String> events = new Array<>();

    private GameState gameState = new GameState();

    private final String JOIN_MSG = "Just joined";
    private final String LEFT_MSG = "Just left";
    private final String SAID_MSG = " said ";

    public GameLoop(MyWebSocketHandler socketHandler, Json json) {
        this.socketHandler = socketHandler;
        this.json = json;
    }

    @Override
    public void create() {

        socketHandler.setConnectListener(session -> {
            events.add(session.getId() + JOIN_MSG);
            if(!gameState.containPlayer(session.getId())){
                gameState.addPlayer(session.getId());
                gameState.updateAmount();
                events.add("playersAmount " + gameState.getPlayersAmount());
            }
        });
        socketHandler.setDisconnectListener(session -> {
            events.add(session.getId() + LEFT_MSG);
            if(gameState.containPlayer(session.getId())){
                gameState.removePlayer(session.getId());
                gameState.updateAmount();
                events.add("playersAmount " + gameState.getPlayersAmount());
            }
        });
        socketHandler.setMessageListener((((session, message) -> {
            String playerInfo = "UPDATE" + session + " " + message;
            System.out.println(message);
            events.add(session.getId() + SAID_MSG + message);
//            events.add("playersAmount " + gameState.getPlayersAmount());
        })));

    }


    @Override
    public void render() {

        for (WebSocketSession session : socketHandler.getSessions()) {
            try {
                for (String event : events) {
                    session.sendMessage(new TextMessage(event));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        events.clear();

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
