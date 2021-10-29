package com.asa.base.ui.browser.widget.console;

/**
 * @author andrew_asa
 * @date 2021/7/5.
 */
public interface JConsoleProcess {

    /**
     * 处理
     * @param input
     * @return
     */
    JConsoleProcessResult process(String input);
}
