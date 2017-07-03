package com.rainbow.red_alert.monitor.util;

public final class ActorUtil {
    private ActorUtil() {
    }

    public static String getActorName(String actorClassName, String actorRealName) {
        return actorClassName + ":" + actorRealName;
    }

    public static String getActorName(Class clazz, String actorRealName) {
        return getActorName(clazz.getSimpleName(), actorRealName);
    }
}
