package com.aqwsxlostfly.packandgo.client;

import com.aqwsxlostfly.packandgo.Main;
import com.aqwsxlostfly.packandgo.client.ws.WsEvent;
import com.badlogic.gdx.Gdx;

public class MessageProcessor {
    private final Main main;

    public MessageProcessor(Main main) {
        this.main = main;
    }

    public void processEvent(WsEvent event) {
        String data = event.getData();
        main.setMeId("777");
        if (data != null) {
          // TODO
            Gdx.app.log("PROCESSED EVENT", data);
        }
    }


//    private void processArray(JSONArray array) {
//        for (int i = 0; i < array.size(); i++) {
//            //TODO
//        }
//    }

//    private void processObject(JSONObject object) {
//       // TODO
//    }
}