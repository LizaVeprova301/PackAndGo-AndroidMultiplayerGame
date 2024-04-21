package com.aqwsxlostfly.packandgo;

public class GameSession {
    private String id;
    private String password;
    private Boolean isConnected;

    private String sessionErrorMsg;
    private Integer playersAmount;

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

    public String getSessionErrorMsg() {
        return sessionErrorMsg;
    }

    public void setSessionErrorMsg(String sessionErrorMsg) {
        this.sessionErrorMsg = sessionErrorMsg;
    }

    public Integer getPlayersAmount() {
        return playersAmount;
    }

    public void setPlayersAmount(Integer playersAmount) {
        this.playersAmount = playersAmount;
    }
}
