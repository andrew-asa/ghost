package com.asa.base.ui.browser.widget.degger.element;

/**
 * @author andrew_asa
 * @date 2021/7/6.
 * 节点元素
 */
public interface WebElement {

    /**
     * 点击
     */
    void click();

    /**
     * 页面上是否存在
     * @return
     */
    boolean exist();

    /**
     * 页面上是否可见
     * @return
     */
    boolean visible();

    /**
     * 等待存在直到超时
     * @param timeOutInSeconds
     * @return
     */
    void waitExistUntil(long timeOutInSeconds,TimeOutCallback<Boolean> callback);
}
