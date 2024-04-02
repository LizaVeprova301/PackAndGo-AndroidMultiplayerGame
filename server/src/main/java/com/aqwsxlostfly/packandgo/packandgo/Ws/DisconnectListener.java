package com.aqwsxlostfly.packandgo.packandgo.Ws;


import org.springframework.web.socket.WebSocketSession;

public interface DisconnectListener {
    void handle(WebSocketSession session);
}
