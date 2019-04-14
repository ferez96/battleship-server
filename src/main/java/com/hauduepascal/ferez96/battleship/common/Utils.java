package com.hauduepascal.ferez96.battleship.common;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {

    public static Map<String, String> parseArgs(String args[]) {
        Map<String, String> ans = new LinkedHashMap<>();
        for (int i = 0; i < args.length; ) {
            String cur = args[i];

            // long param
            if (cur.startsWith("--")) {
                ans.put(cur.substring(2), args[i + 1]);
                i += 2;
                continue;
            }

            // short param
            if (cur.startsWith("-")) {
                ans.put(cur.substring(1), args[i + 1]); // not handled duplicate key
                i += 2;
                continue;
            }

            // default
            {
                ans.put(cur, null);
                i += 1;
            }
        }
        return ans;
    }
}
