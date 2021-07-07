package com.asa.browser.base;

import com.asa.browser.widget.console.JConsoleProcess;
import com.asa.browser.widget.console.JConsoleProcessResult;
import javafx.scene.web.WebEngine;

/**
 * @author andrew_asa
 * @date 2021/7/5.
 */
public class JConsoleWebEngineProcess implements JConsoleProcess {

    private WebEngine webEngine;

    public JConsoleWebEngineProcess(WebEngine webEngine) {

        this.webEngine = webEngine;
    }

    @Override
    public JConsoleProcessResult process(String input) {

        try {
            Object result =  webEngine.executeScript(input);
            if (result!=null && result instanceof String) {
                return JConsoleProcessResult.build((String) result);
            }
        } catch (Exception e) {

        }
        return JConsoleProcessResult.build(input);
    }
}
