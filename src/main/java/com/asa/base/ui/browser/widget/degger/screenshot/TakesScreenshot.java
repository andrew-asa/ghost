package com.asa.base.ui.browser.widget.degger.screenshot;

/**
 * @author andrew_asa
 * @date 2021/7/7.
 */
public interface TakesScreenshot {

    <X> X getScreenshotAs(OutputType<X> type) throws Exception;
}
