package com.asa.browser.widget.console;

/**
 * @author andrew_asa
 * @date 2021/7/5.
 */
public class EchoConsoleProcess implements JConsoleProcess{

    @Override
    public JConsoleProcessResult process(String input) {

        return JConsoleProcessResult.build(input);
    }
}
