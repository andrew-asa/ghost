package com.asa.browser.interrupt;

import com.asa.log.LoggerFactory;
import com.asa.utils.StringUtils;
import com.asa.utils.io.IOUtils;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;
import java.util.List;

/**
 * @author andrew_asa
 * @date 2021/7/4.
 */
public class BrowserInterrupt {

    private WebEngine webEngine;

    private List<String> defaultJs = Arrays.asList("com/asa/browser/interrupt.js","com/asa/browser/ghost_base.js");

    public BrowserInterrupt(WebEngine webEngine) {

        this.webEngine = webEngine;
    }

    public void addDefaultInterruptJs() {

        webEngine.getLoadWorker()
                .stateProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {
                            //System.out.println(newValue);
                            if (newValue == Worker.State.SUCCEEDED) {
                                defaultJs.forEach(item -> {
                                    try {
                                        ClassPathResource resource = new ClassPathResource(item);
                                        String script =  IOUtils.inputStream2String(resource.getInputStream());
                                        if (StringUtils.isNotEmpty(script)) {
                                            webEngine.executeScript(script);
                                        }
                                    } catch (Exception e) {
                                        LoggerFactory.getLogger().warn("error add InterruptJs [{}]", item);
                                    }
                                });
                            }
                        });
    }
}
