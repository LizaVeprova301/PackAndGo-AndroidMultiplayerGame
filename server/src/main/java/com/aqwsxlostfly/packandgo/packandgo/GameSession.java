package com.aqwsxlostfly.packandgo.packandgo;

import com.aqwsxlostfly.packandgo.packandgo.GameState.Player;
import com.badlogic.gdx.utils.ObjectMap;

public class GameSession {

    private String classtype;
    private String sesid;
    private String password;
    private Boolean isConnected;

    private String sessionErrorMsg;
    private Integer playersAmount;

    public final ObjectMap<String, Player> players = new ObjectMap<>();

    public GameSession(String id, String password, Boolean isConnected) {
        this.classtype = "mysession";
        this.sesid = id;
        this.password = password;
        this.isConnected = isConnected;
    }

    public String getId() {
        return sesid;
    }

    public void setId(String id) {
        this.sesid = id;
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

    @Override
    public String toString() {
        return "GameSession{" +
                "Id='" + sesid + '\'' +
                ", password='" + password + '\'' +
                ", isConnected=" + isConnected +
                ", sessionErrorMsg='" + sessionErrorMsg + '\'' +
                ", playersAmount=" + playersAmount +
                ", players=" + players +
                '}';
    }
}

