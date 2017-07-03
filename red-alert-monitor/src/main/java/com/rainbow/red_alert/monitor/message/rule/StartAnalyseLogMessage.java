package com.rainbow.red_alert.monitor.message.rule;

public class StartAnalyseLogMessage {
    private int fromTimestamp;
    private int toTimestamp;

    public StartAnalyseLogMessage(int fromTimestamp, int toTimestamp) {
        this.fromTimestamp = fromTimestamp;
        this.toTimestamp = toTimestamp;
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
