package com.aqwsxlostfly.packandgo.packandgo.Ws;


import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

public interface DisconnectListener {
    void handle(StandardWebSocketSession session);
}
