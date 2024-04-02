package com.aqwsxlostfly.packandgo.packandgo.Ws;

import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

public interface ConnectListener {
    void handle(StandardWebSocketSession session);

}
