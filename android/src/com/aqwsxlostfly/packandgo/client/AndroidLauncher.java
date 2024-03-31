package com.aqwsxlostfly.packandgo.client;

import android.os.Bundle;

import com.aqwsxlostfly.packandgo.Main;
import com.aqwsxlostfly.packandgo.client.dto.InputStateImpl;
import com.aqwsxlostfly.packandgo.client.ws.EventListenerCallback;
import com.aqwsxlostfly.packandgo.client.ws.WebSocketClient;
import com.aqwsxlostfly.packandgo.client.ws.WebSocketListener;
import com.aqwsxlostfly.packandgo.client.ws.WsEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Timer;

import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class AndroidLauncher extends AndroidApplication {
    private MessageProcessor messageProcessor;
    private static final int MAX_RECONNECT_ATTEMPTS = 5;

    public void connectSocket(WebSocketClient webSocketClient) {
        int reconnectAttempts = 0;
        while (!webSocketClient.isOpen() && reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
            try {
                webSocketClient.connect();
                return;
            } catch (Exception e) {
                Gdx.app.error("ERROR SOCKET CONNECT", "Attempt " + (reconnectAttempts + 1) + " failed: " + e.getMessage());
                reconnectAttempts++;
            }
        }

        Gdx.app.error("ERROR SOCKET CONNECT", "Maximum reconnection attempts reached");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        Main main = new Main(new InputStateImpl());

        messageProcessor = new MessageProcessor(main);


        EventListenerCallback callback = event -> {
            messageProcessor.processEvent(event);
        };

//        Timer timer = new Timer();
//        timer.scheduleTask(new Timer.Task() {
//            @Override
//            public void run() {
//                main.handleTimer();
//            }
//        }, 0, 1);
        initialize(main, config);

        String wsUri = readWsUriFromProperties();

        try {
            WebSocketClient webSocketClient = new WebSocketClient(new URI(wsUri), getWebsocketListener(callback));

            connectSocket(webSocketClient);

//            main.setSocketState(webSocketClient.isOpen());
//            Gdx.app.log("WS STATUS", "Status: " + main.getSocketState());

//            if (webSocketClient.isOpen()){
//                main.setSocketState(true);
//            }

            main.setMessageSender(message -> {
                webSocketClient.send(toJson(message));
            });

        } catch (URISyntaxException e) {
            Gdx.app.error("CONNECTION ERROR", "INCORRECT URL: " + e.getMessage());
            throw new RuntimeException(e);
        }


    }

    private WebSocketListener getWebsocketListener(EventListenerCallback callback){
        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onMessageReceived(String message) {
                Gdx.app.log("MESSAGE RECEIVED", "MESSAGE: " + message);
                WsEvent wsEvent = new WsEvent();
                wsEvent.setData(message);
                callback.onEvent(wsEvent);
            }

            @Override
            public void onConnect(ServerHandshake handshake) {
                Gdx.app.log("CONNECTION CREATED","HTTP_STATUS: " + handshake.getHttpStatusMessage());
//                timer.start();
            }

            @Override
            public void onClose(int code, String reason) {
                Gdx.app.log("CONNECTION CLOSED", "CODE: " + code + " REASON: " + reason);
                WsEvent wsEvent = new WsEvent();
                wsEvent.setData("CONNECTION_CLOSED");
                callback.onEvent(wsEvent);
            }

            @Override
            public void onError(Exception ex) {
                Gdx.app.error("CONNECTION ERROR","ERROR_MESSAGE: " + ex.getMessage());
                WsEvent wsEvent = new WsEvent();
                wsEvent.setData("ERROR_OCCURED");
                callback.onEvent(wsEvent);
            }
        };

        return webSocketListener;
    }

    private String toJson(Object object){
        Json json = new Json();
        return json.toJson(object);
    }


    private String readWsUriFromProperties() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = getAssets().open("env.properties");
            properties.load(inputStream);
            return properties.getProperty("WS_URI");
        } catch (IOException e) {
            Gdx.app.error("ERROR ENV", "Failed to read WS_URI from properties file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public AndroidApplicationConfiguration getConfig() {
        return new AndroidApplicationConfiguration();
    }

}
