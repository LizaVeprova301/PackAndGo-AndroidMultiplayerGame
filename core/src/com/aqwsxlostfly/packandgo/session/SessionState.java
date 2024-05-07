package com.aqwsxlostfly.packandgo.session;

public interface SessionState {

    void setType(String type);
    String getType();
    void setSessionId(String id);
    String getSessionId();

    void setSessionPassword(String password);
    String getSessionPassword();


}
