package com.asa.browser.base;


import com.asa.browser.widget.degger.WebElement;
import com.asa.browser.widget.degger.selector.By;

/**
 * @author andrew_asa
 * @date 2021/7/6.
 */
public interface JBrowserDebugger {

    Object executeScript(String script);

    WebElement findElement(By selector);
}
