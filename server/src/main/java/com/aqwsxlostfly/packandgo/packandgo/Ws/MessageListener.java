package com.aqwsxlostfly.packandgo.packandgo.Ws;


import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.socket.WebSocketSession;

public interface MessageListener {
    void handle(WebSocketSession session, JsonNode message);
}

