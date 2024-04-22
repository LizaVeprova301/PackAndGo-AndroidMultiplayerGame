package com.aqwsxlostfly.packandgo.client.ws;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketClient extends org.java_websocket.client.WebSocketClient {
    private final WebSocketListener webSocketListener;

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
