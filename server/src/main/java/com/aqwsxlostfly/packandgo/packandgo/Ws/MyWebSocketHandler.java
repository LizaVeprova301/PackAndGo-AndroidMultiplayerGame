package com.aqwsxlostfly.packandgo.packandgo.Ws;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import static com.aqwsxlostfly.packandgo.packandgo.Constants.LogConstants.*;

@Slf4j
@Component
public class MyWebSocketHandler extends AbstractWebSocketHandler {

    private final Array<StandardWebSocketSession> sessions = new Array<>();
    private final ObjectMapper mapper;
    private ConnectListener connectListener;
    private DisconnectListener disconnectListener;
    private MessageListener messageListener;

    public MyWebSocketHandler() {
        this.mapper = new ObjectMapper();
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        StandardWebSocketSession standardWebSocketSession = (StandardWebSocketSession) session;

        synchronized (sessions) {
            sessions.add(standardWebSocketSession);
            connectListener.handle(standardWebSocketSession);
        }

        Gdx.app.log(CONNECTION_TAG, " NEW CONNECTION: " + " sessionID " + session.getId() + " headers " +
                session.getHandshakeHeaders() + " protocols " + session.getAcceptedProtocol() + " sizeBynaryLimit " +
                session.getBinaryMessageSizeLimit() + " clientIP " + session.getRemoteAddress());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        Gdx.app.log(MESSAGE_TAG, " NEW MESSAGE: " + message.getPayload() + "\n\n" + " sessionID " + session.getId()
                + " headers " + session.getHandshakeHeaders() + " protocols " + session.getAcceptedProtocol()
                + " sizeTextLimit " + session.getTextMessageSizeLimit() + " clientIP " + session.getRemoteAddress());

        StandardWebSocketSession standardWebSocketSession = (StandardWebSocketSession) session;

        String payload = message.getPayload();
        JsonNode jsonNode = mapper.readTree(payload);

        messageListener.handle(standardWebSocketSession, jsonNode);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        StandardWebSocketSession standardWebSocketSession = (StandardWebSocketSession) session;

        synchronized (sessions) {
            sessions.removeValue(standardWebSocketSession, true);
            disconnectListener.handle(standardWebSocketSession);
        }

        Gdx.app.log(DISCONNECTION_TAG, " CLOSED CONNECTION: " + " sessionID " + session.getId() + " headers " +
                session.getHandshakeHeaders() + " protocols " + session.getAcceptedProtocol() + " sizeBynaryLimit " +
                session.getBinaryMessageSizeLimit() + " clientIP " + session.getRemoteAddress());
    }


    public Array<StandardWebSocketSession> getSessions() {
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
