package com.asa.browser.widget.degger.selector;

import java.io.Serializable;

/**
 * @author andrew_asa
 * @date 2021/7/7.
 */
public  class ById extends By implements Serializable {
    private final String id;

    public ById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cannot find elements when the id is null.");
        } else {
            this.id = id;
        }
    }

    public String toString() {
        return "By.id: " + this.id;
    }

    @Override
    public String getSelector() {

        return "#"+id;
    }
}
