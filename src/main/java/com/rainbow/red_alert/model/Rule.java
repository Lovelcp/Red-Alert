package com.rainbow.red_alert.model;

public class Rule {
    private String topic;
    private String keyword;

    public Rule(String topic, String keyword) {
        this.topic = topic;
        this.keyword = keyword;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
