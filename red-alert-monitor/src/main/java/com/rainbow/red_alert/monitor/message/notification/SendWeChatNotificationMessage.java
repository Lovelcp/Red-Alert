package com.rainbow.red_alert.monitor.message.notification;

public class SendWeChatNotificationMessage {
    private String scKey;
    private String title;
    private String description;

    public SendWeChatNotificationMessage(String scKey, String title, String description) {
        this.scKey = scKey;
        this.title = title;
        this.description = description;
    }

    public String getScKey() {
        return scKey;
    }

    public void setScKey(String scKey) {
        this.scKey = scKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
