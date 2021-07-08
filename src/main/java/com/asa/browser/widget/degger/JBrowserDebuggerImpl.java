package com.asa.browser.widget.degger;

import com.asa.browser.base.JBrowserDebugger;
import com.asa.browser.widget.degger.element.WebElement;
import com.asa.browser.widget.degger.selector.By;
import com.asa.utils.StringUtils;
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
    public Object executeScriptIgnoreException(String script) {

        try {
            return webEngine.executeScript(script);
        } catch (Exception e) {

        }
        return null;
    }


    @Override
    public WebElement findElement(By selector) {

        return new WebElementImpl(this, selector);
    }

    @Override
    public String getCookie() {

        Object c = executeScriptIgnoreException("document.cookie");
        if (c != null && c instanceof String) {
            return c.toString();
        }
        return StringUtils.EMPTY;
    }
}
