package com.asa.browser.widget.degger.element;

/**
 * @author andrew_asa
 * @date 2021/7/8.
 */
public interface Sleeper {
    Sleeper SYSTEM_SLEEPER = (duration) -> {
        Thread.sleep(duration.toMillis());
    };

    void sleep(java.time.Duration duration) throws InterruptedException;
}
