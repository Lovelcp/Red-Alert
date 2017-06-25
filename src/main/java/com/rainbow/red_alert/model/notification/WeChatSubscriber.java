package com.rainbow.red_alert.model.notification;

public class WeChatSubscriber extends BaseSubscriber {
    private String serverChanSckey;

    public WeChatSubscriber(String name, String serverChanSckey) {
        super(name);
        this.serverChanSckey = serverChanSckey;
    }

    public String getServerChanSckey() {
        return serverChanSckey;
    }

    public void setServerChanSckey(String serverChanSckey) {
        this.serverChanSckey = serverChanSckey;
    }
}
