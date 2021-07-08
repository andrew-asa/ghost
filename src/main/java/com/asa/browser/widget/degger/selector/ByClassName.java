package com.asa.browser.widget.degger.selector;

import java.io.Serializable;

/**
 * @author andrew_asa
 * @date 2021/7/7.
 */
public  class ByClassName extends By implements Serializable {
    private final String className;

    public ByClassName(String className) {
        if (className == null) {
            throw new IllegalArgumentException("Cannot find elements when the class name expression is null.");
        } else {
            this.className = className;
        }
    }



    public String toString() {
        return "By.className: " + this.className;
    }

    @Override
    public String getSelector() {

        return "." + className;
    }
}