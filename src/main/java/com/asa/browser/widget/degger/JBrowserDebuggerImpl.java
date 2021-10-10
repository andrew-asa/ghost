package com.asa.browser.widget.degger;

import com.asa.browser.base.JBrowserDebugger;
import com.asa.browser.widget.degger.element.WebElement;
import com.asa.browser.widget.degger.selector.By;
import com.asa.base.log.LoggerFactory;
import com.asa.base.utils.ListUtils;
import com.asa.base.utils.StringUtils;
import com.sun.webkit.network.CookieManager;
import javafx.scene.web.WebEngine;

import java.net.CookieHandler;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        //Object c = executeScriptIgnoreException("document.cookie");
        //if (c != null && c instanceof String) {
        //    return c.toString();
        //}
        CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
        try {
            Map<String, List<String>> result = cookieManager.get(new URI(webEngine.getLocation()), new HashMap<>());
            List<String> cookies = result.get("Cookie");
            if (ListUtils.isNotEmpty(cookies)) {
                return cookies.get(0);
            }
        }catch (Exception e) {
            LoggerFactory.getLogger().debug("error get cookie from CookieManager");
        }
        return StringUtils.EMPTY;
    }
}
