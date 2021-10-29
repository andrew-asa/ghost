package com.asa.browser;

import com.asa.base.ui.browser.Browser;
import com.asa.base.ui.browser.base.JBrowserDebugger;
import com.asa.base.ui.browser.widget.degger.element.WebElement;
import com.asa.base.ui.browser.widget.degger.selector.By;
import com.asa.base.log.LoggerFactory;
import com.asa.base.utils.StringUtils;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author andrew_asa
 * @date 2021/7/8.
 */
public class TestWeixinLogin extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle(this.getClass().getName());
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
        LoggerFactory.getLogger().debug("start login");
        JBrowserDebugger debugger = browser.getDebugger();
        WebElement element = debugger.findElement(By.className("weui-desktop-account__info"));
        element.waitExistUntil(30, (back)-> {
            if (back) {
                String cookie = debugger.getCookie();
                output.setText(cookie);
            }
        });
        return bp;
    }
}
