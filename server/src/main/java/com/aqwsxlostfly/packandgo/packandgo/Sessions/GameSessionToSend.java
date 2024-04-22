package com.aqwsxlostfly.packandgo.packandgo.Sessions;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class GameSessionToSend implements Json.Serializable {
    private String sessionId;
    private String sessionPassword;
    private String sessionMsg;

    public GameSessionToSend(String sessionId, String sessionPassword, String sessionMsg) {

        this.sessionId = sessionId;
        this.sessionPassword = sessionPassword;
        this.sessionMsg = sessionMsg;
    }

    @Override
    public void write(Json json) {
        json.writeValue("id", sessionId);
        json.writeValue("password", sessionPassword);
        json.writeValue("msg", sessionMsg);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionPassword() {
        return sessionPassword;
    }

    public void setSessionPassword(String sessionPassword) {
        this.sessionPassword = sessionPassword;
    }

    public String getSessionMsg() {
        return sessionMsg;
    }

    public void setSessionMsg(String sessionMsg) {
        this.sessionMsg = sessionMsg;
    }
}
