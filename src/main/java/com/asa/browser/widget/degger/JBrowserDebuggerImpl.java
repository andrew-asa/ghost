package com.asa.browser.widget.degger;

import com.asa.browser.base.JBrowserDebugger;
import com.asa.browser.widget.degger.selector.By;
import javafx.scene.web.WebEngine;

/**
 * @author andrew_asa
 * @date 2021/7/6.
 */
public class JBrowserDebuggerImpl implements JBrowserDebugger {

    private WebEngine webEngine;

    public JBrowserDebuggerImpl(WebEngine webEngine) {

        this.webEngine = webEngine;
    }

    @Override
    public Object executeScript(String script) {

        return webEngine.executeScript(script);
    }

    @Override
    public WebElement findElement(By selector) {

        return null;
    }
}
