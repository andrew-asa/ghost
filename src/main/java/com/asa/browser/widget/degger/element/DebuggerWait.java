package com.asa.browser.widget.degger.element;

import com.asa.log.LoggerFactory;
import javafx.application.Platform;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

/**
 * @author andrew_asa
 * @date 2021/7/8.
 */
public class DebuggerWait {

    private WebElement element;

    private Duration timeout;

    private Duration interval;

    private Clock clock;

    private Sleeper sleeper;

    public DebuggerWait(WebElement element, long timeoutInSeconds) {

        this(element, Clock.systemDefaultZone(), timeoutInSeconds, 500L, Sleeper.SYSTEM_SLEEPER);
    }

    public DebuggerWait(WebElement element, Clock clock, long timeoutInSeconds, long sleepTimeOutInMillis, Sleeper sleeper) {

        this.element = element;
        this.clock = clock;
        this.timeout = Duration.ofSeconds(timeoutInSeconds);
        this.interval = Duration.ofMillis(sleepTimeOutInMillis);
        this.sleeper = sleeper;

    }

    public void until(Function<WebElement, Boolean> isTrue, TimeOutCallback<Boolean> callback) {

        Instant end = this.clock.instant().plus(this.timeout);
        until(isTrue, callback, end);
        //while(true) {
        //    try {
        //        if (isTrue.apply(element)) {
        //            callback.accept(true);
        //            LoggerFactory.getLogger().debug("DebuggerWait pass");
        //        }
        //    } catch (Throwable ex) {
        //        //    do nothing
        //    }
        //
        //    if (end.isBefore(this.clock.instant())) {
        //        callback.accept(false);
        //    }
        //
        //    try {
        //        //
        //        LoggerFactory.getLogger().debug("{}--sleep--{}",System.currentTimeMillis(),interval.toMillis());
        //        this.sleeper.sleep(this.interval);
        //    } catch (InterruptedException ex) {
        //        //Thread.currentThread().interrupt();
        //        //return false;
        //    }
        //}
    }

    private void until(Function<WebElement, Boolean> isTrue, TimeOutCallback<Boolean> callback, Instant end) {

        Platform.runLater(() -> {
            if (isTrue.apply(element)) {
                callback.accept(true);
                LoggerFactory.getLogger().debug("DebuggerWait pass");
                return;
            } else if (end.isBefore(this.clock.instant())) {
                LoggerFactory.getLogger().debug("DebuggerWait timeout");
                callback.accept(false);
            } else {
                //LoggerFactory.getLogger().debug("add DebuggerWait call ");
                new Timer("timer - ").schedule(new TimerTask() {

                    @Override
                    public void run() {

                        LoggerFactory.getLogger().debug("DebuggerWait call again");
                        until(isTrue, callback, end);
                    }
                }, interval.toMillis());
                //LoggerFactory.getLogger().debug("end add DebuggerWait call ");
            }
        });
    }
}
