package com.asa.browser;

import com.asa.base.utils.StringUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author andrew_asa
 * @date 2021/7/5.
 */
public class TestTextField extends Application {

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
        TextField tf = new TextField();
        vBox.getChildren().add(tf);
        return vBox;
    }
}
