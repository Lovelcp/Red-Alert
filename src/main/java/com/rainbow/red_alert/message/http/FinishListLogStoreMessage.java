package com.rainbow.red_alert.message.http;

import java.util.ArrayList;

public class FinishListLogStoreMessage {
    private ArrayList<String> logStores;

    public ArrayList<String> getLogStores() {
        return logStores;
    }

    public void setLogStores(ArrayList<String> logStores) {
        this.logStores = logStores;
    }

    public FinishListLogStoreMessage(ArrayList<String> logStores) {
        this.logStores = logStores;
    }
}
