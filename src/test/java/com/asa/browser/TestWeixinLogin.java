package com.asa.browser;

import com.asa.browser.base.JBrowserDebugger;
import com.asa.browser.widget.degger.element.TimeOutCallback;
import com.asa.browser.widget.degger.element.WebElement;
import com.asa.browser.widget.degger.selector.By;
import com.asa.log.LoggerFactory;
import com.asa.utils.StringUtils;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;

/**
 * @author andrew_asa
 * @date 2021/7/8.
 */
public class TestWeixinLogin extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("jbrowser");
        primaryStage.setScene(getRootScene());
        primaryStage.show();
    }

    public Scene getRootScene() {

        return new Scene(getContentNode(), 1024, 860);
    }

    public Parent getContentNode() {

        VBox vBox = new VBox();
        JFXTextArea script = new JFXTextArea();
        JFXTextArea output = new JFXTextArea();
        HBox bb = new HBox();
        Button exec = new Button("执行");
        Button load = new Button("加载");
        Button reload = new Button("重新加载");
        Button startLogin = new Button("登录");
        bb.getChildren().addAll(exec, load,reload,startLogin);
        vBox.getChildren().addAll(script,bb,output);

        BorderPane bp = new BorderPane();
        Browser browser = new Browser();
        browser.load("https://mp.weixin.qq.com/");
        bp.setTop(vBox);
        bp.setCenter(browser);
        exec.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                String text = script.getText();
                if (StringUtils.isNotEmpty(text) ) {
                    try {
                        Object ret = browser.getDebugger().executeScript(text);
                        if (ret != null ) {
                            output.setText(ret.toString());
                        }
                    } catch (Exception e) {
                        output.setText(e.getMessage());
                    }
                }
            }
        });

        load.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String text = script.getText();
                if (StringUtils.isNotEmpty(text) && StringUtils.startsWith(text, "http")) {
                    browser.load(text);
                }
            }
        });
        reload.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                browser.load(browser.getLocation());
            }
        });
        new Thread(()->{
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LoggerFactory.getLogger().debug("start log");
            Platform.runLater(()->{
                JBrowserDebugger debugger = browser.getDebugger();
                WebElement element = debugger.findElement(By.className("weui-desktop-account__info"));
                element.waitExistUntil(30, (back)-> {
                    if (back) {
                        String cookie = debugger.getCookie();
                        output.setText(cookie);
                    }
                });
            });
        }).start();


        return bp;
    }
}
