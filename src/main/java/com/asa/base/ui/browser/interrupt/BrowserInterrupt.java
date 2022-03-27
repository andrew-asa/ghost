package com.asa.base.ui.browser.interrupt;

import com.asa.base.log.LoggerFactory;
import com.asa.base.utils.StringUtils;
import com.asa.base.utils.io.IOUtils;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author andrew_asa
 * @date 2021/7/4.
 */
public class BrowserInterrupt {

    private WebEngine webEngine;

    private List<String> defaultJs = Arrays.asList("com/asa/base/ui/browser/interrupt.js", "com/asa/base/ui/browser/ghost_base.js");

    public BrowserInterrupt(WebEngine webEngine) {

        this.webEngine = webEngine;
    }

    public void addDefaultInterruptJs() {

        webEngine.getLoadWorker()
                .stateProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {
                            if (newValue == Worker.State.SUCCEEDED) {
                                // 当前作用域里面有了就不加载
                                try {
                                    webEngine.executeScript("ghost");
                                } catch (Throwable throwable) {
                                    List<String> js = getInterruptJs();
                                    js.forEach(item -> {
                                        try {
                                            ClassPathResource resource = new ClassPathResource(item);
                                            String script = IOUtils.inputStream2String(resource.getInputStream());
                                            if (StringUtils.isNotEmpty(script)) {
                                                webEngine.executeScript(script);
                                            }
                                            LoggerFactory.getLogger().debug("add interrupt js {}", item);
                                        } catch (Exception e) {
                                            LoggerFactory.getLogger().warn("error add InterruptJs [{}]", item);
                                        }
                                    });
                                }
                            }
                        });
    }

    private List<String> getInterruptJs() {

        List<String> ret = new ArrayList<>();
        ret.addAll(defaultJs);
        // 放入插件的
        return ret;
    }
}
