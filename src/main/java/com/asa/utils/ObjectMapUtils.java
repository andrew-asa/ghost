package com.asa.utils;

import java.util.Arrays;
import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/10/2.
 */
public class ObjectMapUtils {

    /**
     * 获取object里面的map
     *
     * @param om
     * @param items
     * @return
     */
    public static Map getMap(Map om, String... items) {

        if (items.length == 0) {
            return om;
        }
        String lastKey = items[items.length - 1];
        Map cache = getLastMap(om, items);
        Object r = MapUtils.get(cache, lastKey);
        if (r != null && r instanceof Map) {
            return (Map) r;
        }
        return null;
    }

    /**
     * 获取字符串字段
     *
     * @param om
     * @param items
     * @return
     */
    public static String getString(Map om, String... items) {

        if (items.length == 0) {
            return null;
        }
        Map lastMap = getLastMap(om, items);
        String ret = null;
        String lastKey = items[items.length - 1];
        if (MapUtils.containsKey(lastMap, lastKey)) {
            return String.valueOf(lastMap.get(lastKey));
        }
        return ret;
    }

    /**
     * 获取倒数最后一个值的map
     *
     * @param om
     * @param items
     * @return 如果没有达到长度链的要求则直接返回null
     */
    private static Map getLastMap(Map om, String... items) {

        if (om == null) {
            return null;
        }
        if (items.length == 1) {
            return om;
        }
        Map lastMap = om;
        if (items.length > 0) {
            String[] lastMapIndex = Arrays.copyOfRange(items, 0, items.length - 1);
            for (String key : lastMapIndex) {
                Object v = MapUtils.get(lastMap, key);
                if (v == null || !(v instanceof Map)) {
                    lastMap = null;
                    break;
                }
                lastMap = (Map) v;
            }
        }
        return lastMap;
    }
}
