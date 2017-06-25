package com.rainbow.red_alert.model.notification;

public class BaseSubscriber {
    private String name;

    public BaseSubscriber(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
