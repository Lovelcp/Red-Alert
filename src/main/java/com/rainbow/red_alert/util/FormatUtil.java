package com.rainbow.red_alert.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class FormatUtil {
    private FormatUtil() {
    }

    public static String readableTime(int timestamp) {
        Date date = new Date((long) timestamp * 1000);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

}
