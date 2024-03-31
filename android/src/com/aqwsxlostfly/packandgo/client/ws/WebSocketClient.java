package com.aqwsxlostfly.packandgo.client.ws;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketClient extends org.java_websocket.client.WebSocketClient {
    private WebSocketListener webSocketListener;

    private static final int MAX_RECONNECT_ATTEMPTS = 3;

    //    public void connectSocket() {
//        isConnecting = true;
//        int reconnectAttempts = 0;
//        while (reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
//            try {
//                main.webSocketClient.connect();
//                isConnecting = false;
//                return;
//            } catch (Exception e) {
//                Gdx.app.error("ERROR SOCKET CONNECT", "Attempt " + (reconnectAttempts + 1) + " failed: " + e.getMessage());
//                reconnectAttempts++;
//            }
//        }
//
//        isConnecting = false;
//
//        Gdx.app.error("ERROR SOCKET CONNECT", "Maximum reconnection attempts reached");
//    }

    public WebSocketClient(URI serverUri, WebSocketListener webSocketListener) {
        super(serverUri);
        this.webSocketListener = webSocketListener;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        webSocketListener.onConnect(handshake);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        webSocketListener.onClose(code, reason);
    }

    @Override
    public void onMessage(String message) {
        webSocketListener.onMessageReceived(message);
    }

    @Override
    public void onError(Exception ex) {
        webSocketListener.onError(ex);
    }
}
