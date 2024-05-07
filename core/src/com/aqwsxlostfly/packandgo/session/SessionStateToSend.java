package com.aqwsxlostfly.packandgo.session;

import com.aqwsxlostfly.packandgo.session.SessionState;

public class SessionStateToSend implements SessionState {

    private String type;
    private String Id;
    private String password;

    public SessionStateToSend(String type, String id, String password) {
        this.type = type;
        Id = id;
        this.password = password;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setSessionId(String id) {
        this.Id = id;
    }

    @Override
    public String getSessionId() {
        return this.Id;
    }

    @Override
    public void setSessionPassword(String password) {
        this.password = password;
    }

    @Override
    public String getSessionPassword() {
        return this.password;
    }
}
