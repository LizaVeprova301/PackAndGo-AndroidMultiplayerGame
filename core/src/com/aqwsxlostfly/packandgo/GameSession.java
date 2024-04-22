package com.aqwsxlostfly.packandgo;

public class GameSession {
    private String id;
    private String password;
    private Boolean isConnected;
    private String sessionMsg;

    public GameSession(Boolean isConnected) {
        this.isConnected = isConnected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    public String getSessionMsg() {
        return sessionMsg;
    }

    public void setSessionMsg(String sessionMsg) {
        this.sessionMsg = sessionMsg;
    }
}
