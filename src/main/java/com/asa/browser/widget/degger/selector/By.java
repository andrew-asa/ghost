package com.asa.browser.widget.degger.selector;

/**
 * @author andrew_asa
 * @date 2021/7/7.
 */
public abstract class By {

    public static By id(String id) {
        return new ById(id);
    }

    public static By xpath(String xpathExpression) {
        return new ByXPath(xpathExpression);
    }
}
