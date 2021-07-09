package com.asa.browser.widget.degger;

import com.asa.browser.base.BrowserReturnUtils;
import com.asa.browser.base.JBrowserDebugger;
import com.asa.browser.widget.degger.element.DebuggerWait;
import com.asa.browser.widget.degger.element.TimeOutCallback;
import com.asa.browser.widget.degger.element.WebElement;
import com.asa.browser.widget.degger.screenshot.OutputType;
import com.asa.browser.widget.degger.selector.By;


/**
 * @author andrew_asa
 * @date 2021/7/7.
 */
public class WebElementImpl implements WebElement {

    private JBrowserDebugger debugger;

    private By selector;

    public WebElementImpl(JBrowserDebugger debugger, By selector) {

        this.debugger = debugger;
        this.selector = selector;
    }

    @Override
    public void click() {

        debugger.executeScriptIgnoreException(selector.buildScript("click()"));
    }

    @Override
    public boolean exist() {

        if (selector != null) {
            String script = selector.buildScript("length>0");
            Object ret = debugger.executeScript(script);
            return BrowserReturnUtils.toBoolean(ret);
        }
        return false;
    }

    @Override
    public boolean visible() {

        return BrowserReturnUtils.toBoolean(selector.buildScript("is(\":hidden\")"));
    }

    @Override
    public void waitExistUntil(long timeOutInSeconds, TimeOutCallback<Boolean> callback) {

        DebuggerWait wait = new DebuggerWait(this, timeOutInSeconds);
        wait.until((element)-> {
            try {
                return element.exist();
            } catch (Exception ex) {
                return false;
            }
        },callback);

    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> type) throws Exception {

        return null;
    }
}
