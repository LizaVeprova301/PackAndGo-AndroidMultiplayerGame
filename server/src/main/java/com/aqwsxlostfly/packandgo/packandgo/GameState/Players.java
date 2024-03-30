package com.aqwsxlostfly.packandgo.packandgo.GameState;

import jakarta.websocket.Session;

import java.util.HashMap;

public class Players {

    public int playersAmount = 0;

    public HashMap<String, Integer> players = new HashMap<>();

    public void updateAmount(){
        playersAmount = players.size();
    }

    public void addPlayer(String sessionID){
        players.put(sessionID, 1);
    }

    public void removePlayer(String sessionID){
        players.remove(sessionID);
    }

    public boolean containPlayer(String sessionID){
        return players.containsKey(sessionID);
    }

    public int getPlayersAmount() {
        return playersAmount;
    }

    public void setPlayersAmount(int playersAmount) {
        this.playersAmount = playersAmount;
    }

    public HashMap<String, Integer> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, Integer> players) {
        this.players = players;
    }
}
