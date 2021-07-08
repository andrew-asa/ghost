package com.asa.browser.base;


import com.asa.browser.widget.degger.element.WebElement;
import com.asa.browser.widget.degger.selector.By;

/**
 * @author andrew_asa
 * @date 2021/7/6.
 */
public interface JBrowserDebugger {

    /**
     * 执行指定脚本
     * @param script js脚本
     * @return
     */
    Object executeScript(String script);

    /**
     * 没有异常的执行指定脚本
     * @param script js脚本
     * @return
     */
    Object executeScriptIgnoreException(String script);

    /**
     * 查找元素
     * @param selector 元素选择器
     * @return
     */
    WebElement findElement(By selector);



    /**
     * 获取cookie字符串
     * @return
     */
    String getCookie();
}
