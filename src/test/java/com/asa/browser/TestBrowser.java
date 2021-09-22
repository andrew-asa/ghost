package com.asa.browser;

import com.asa.utils.StringUtils;
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
 * @date 2021/7/4.
 */
public class TestBrowser extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle(this.getClass().getName());
        primaryStage.setScene(getRootScene());
        primaryStage.show();
    }

    public Scene getRootScene() {

        return new Scene(getContentNode(), 1024, 560);
    }

    public Parent getContentNode() {

        VBox vBox = new VBox();
        JFXTextArea script = new JFXTextArea();
        JFXTextArea output = new JFXTextArea();
        HBox bb = new HBox();
        Button exec = new Button("执行");
        Button load = new Button("加载");
        Button reload = new Button("重新加载");
        bb.getChildren().addAll(exec, load,reload);
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
        return bp;
    }
}
