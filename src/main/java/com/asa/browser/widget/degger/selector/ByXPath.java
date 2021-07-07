package com.asa.browser.widget.degger.selector;

import java.io.Serializable;

/**
 * @author andrew_asa
 * @date 2021/7/7.
 */
public class ByXPath extends By implements Serializable {

    private final String xpathExpression;

    public ByXPath(String xpathExpression) {

        if (xpathExpression == null) {
            throw new IllegalArgumentException("Cannot find elements when the XPath is null.");
        } else {
            this.xpathExpression = xpathExpression;
        }
    }

    public String toString() {

        return "By.xpath: " + this.xpathExpression;
    }
}
