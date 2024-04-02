package com.aqwsxlostfly.packandgo.packandgo.Ws;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Getter
@Component
public class MyWebSocketHandler extends AbstractWebSocketHandler {

    private final Array<WebSocketSession> sessions = new Array<>();

    private final ObjectMapper mapper;
    private ConnectListener connectListener;
    private DisconnectListener disconnectListener;
    private MessageListener messageListener;

    public MyWebSocketHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        StandardWebSocketSession standardWebSocketSession = (StandardWebSocketSession) session;
        Gdx.app.log("CONNECTION", " NEW CONNECTION: " + " sessionID " + session.getId() + " headers " +
                session.getHandshakeHeaders() + " protocols " + session.getAcceptedProtocol() + " sizeBynaryLimit " +
                session.getBinaryMessageSizeLimit() + " clientIP " + session.getRemoteAddress());
        synchronized (sessions) {
            sessions.add(standardWebSocketSession);
            connectListener.handle(standardWebSocketSession);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        StandardWebSocketSession standardWebSocketSession = (StandardWebSocketSession) session;
        Gdx.app.log("MESSAGE", " NEW MESSAGE: " + message + "\n\n" + " sessionID " + session.getId()
                + " headers " + session.getHandshakeHeaders() + " protocols " + session.getAcceptedProtocol()
                + " sizeTextLimit " + session.getTextMessageSizeLimit() + " clientIP " + session.getRemoteAddress());

        String payload = message.getPayload();
        JsonNode jsonNode = mapper.readTree(payload);
        messageListener.handle(standardWebSocketSession, message.getPayload());
    }
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        Gdx.app.log("MESSAGE", " NEW MESSAGE: " + message + "\n\n" + " sessionID " + session.getId()
                + " headers " + session.getHandshakeHeaders() + " protocols " + session.getAcceptedProtocol()
                + " sizeTextLimit " + session.getTextMessageSizeLimit() + " clientIP " + session.getRemoteAddress());
        messageListener.handle(session, String.valueOf(message.getPayload()));
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        StandardWebSocketSession standardWebSocketSession = (StandardWebSocketSession) session;
        Gdx.app.log("CLOSED CONNECTION", " CLOSED CONNECTION: " + " sessionID " + session.getId() + " headers " +
                session.getHandshakeHeaders() + " protocols " + session.getAcceptedProtocol() + " sizeBynaryLimit " +
                session.getBinaryMessageSizeLimit() + " clientIP " + session.getRemoteAddress());
        synchronized (sessions) {
            sessions.removeValue(standardWebSocketSession, true);
            disconnectListener.handle(standardWebSocketSession);
        }
    }


    public Array<WebSocketSession> getSessions() {
        return sessions;
    }

    public void setConnectListener(ConnectListener connectListener) {
        this.connectListener = connectListener;
    }

    public void setDisconnectListener(DisconnectListener disconnectListener) {
        this.disconnectListener = disconnectListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

}
