package com.aqwsxlostfly.packandgo.packandgo;

public class GameSessionImpl {
    private String classtype;
    private String sesid;
    private String password;
    private Boolean isConnected;
    private String sessionErrorMsg;
    private Integer playersAmount;

    public GameSessionImpl(String classtype, String sesid, String password, Boolean isConnected) {
        this.classtype = classtype;
        this.sesid = sesid;
        this.password = password;
        this.isConnected = isConnected;
    }

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype;
    }

    public String getSesid() {
        return sesid;
    }

    public void setSesid(String sesid) {
        this.sesid = sesid;
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
