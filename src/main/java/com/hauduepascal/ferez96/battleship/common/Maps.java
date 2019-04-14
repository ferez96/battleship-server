package com.hauduepascal.ferez96.battleship.common;

import java.util.Map;

public class Maps {
    public static int getOrDefault(Map<String, String> m, String key, int defaultValue) {
        String ans = m.getOrDefault(key, null);
        return (ans == null) ? defaultValue : Integer.valueOf(ans);
    }

    public static boolean getOrDefault(Map<String, String> m, String key, boolean defaultValue) {
        String ans = m.getOrDefault(key, null);
        return (ans == null) ? defaultValue : Boolean.valueOf(ans);
    }

    public static String getOrDefault(Map<String, String> m, String key, String defaultValue) {
        return m.getOrDefault(key, defaultValue);
    }
}
