package com.rainbow.red_alert.message.notification;

import com.rainbow.red_alert.model.Rule;

public class StartSendNotificationMessage {
    private String logProjectName;
    private String logStoreName;
    private Rule rule;
    private int fromTimestamp;
    private int toTimestamp;

    public StartSendNotificationMessage(String logProjectName, String logStoreName, Rule rule, int fromTimestamp, int toTimestamp) {
        this.logProjectName = logProjectName;
        this.logStoreName = logStoreName;
        this.rule = rule;
        this.fromTimestamp = fromTimestamp;
        this.toTimestamp = toTimestamp;
    }

    public String getLogProjectName() {
        return logProjectName;
    }

    public void setLogProjectName(String logProjectName) {
        this.logProjectName = logProjectName;
    }

    public String getLogStoreName() {
        return logStoreName;
    }

    public void setLogStoreName(String logStoreName) {
        this.logStoreName = logStoreName;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public int getFromTimestamp() {
        return fromTimestamp;
    }

    public void setFromTimestamp(int fromTimestamp) {
        this.fromTimestamp = fromTimestamp;
    }

    public int getToTimestamp() {
        return toTimestamp;
    }

    public void setToTimestamp(int toTimestamp) {
        this.toTimestamp = toTimestamp;
    }
}
