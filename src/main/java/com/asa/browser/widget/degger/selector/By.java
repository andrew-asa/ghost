package com.asa.browser.widget.degger.selector;

import com.asa.base.utils.StringUtils;

/**
 * @author andrew_asa
 * @date 2021/7/7.
 */
public abstract class By {

    public String buildScript(String function) {

        return StringUtils.messageFormat("$(\"{}\").{}", getSelector(), function);
    }

    public static By id(String id) {

        return new ById(id);
    }

    public static By className(String className) {

        return new ByClassName(className);
    }

    public abstract String getSelector();
}
