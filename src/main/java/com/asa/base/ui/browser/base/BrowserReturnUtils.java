package com.asa.base.ui.browser.base;

import com.asa.base.utils.BooleanUtils;

/**
 * @author andrew_asa
 * @date 2021/7/8.
 */
public class BrowserReturnUtils {

    public static boolean toBoolean(Object ret, boolean defaultValue) {

        if (ret != null) {
            if (ret instanceof Boolean) {
                return (Boolean) ret;
            } else if (ret instanceof String) {
                return BooleanUtils.toBoolean(((String) ret));
            }
        }
        return defaultValue;
    }

    public static boolean toBoolean(Object ret) {

        return toBoolean(ret, false);
    }
}
