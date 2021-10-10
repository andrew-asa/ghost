package com.asa.browser;

import com.asa.base.log.LoggerFactory;
import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.KeyStroke;

/**
 * @author andrew_asa
 * @date 2021/7/5.
 */
public class TestStageHideAndShow extends Application implements HotKeyListener {
    private Stage primaryStage;

    private Provider provider;

    private void showStage(Stage stage) {
        stage.setAlwaysOnTop(true);
        stage.show();
        stage.toFront();
        stage.centerOnScreen();
        //stage.setMaximized(true);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        stage.setTitle(this.getClass().getName());
        stage.setScene(getRootScene());

        showStage(stage);
        Platform.setImplicitExit(false);
        stage.setOnCloseRequest(e -> closeStage());
        registerHotkey();
    }

    private void registerHotkey() {
        provider = Provider.getCurrentProvider(false);
        provider.register(KeyStroke.getKeyStroke("control 3"), new HotKeyListener() {
            public void onHotKey(HotKey hotKey) {

                LoggerFactory.getLogger().debug("{}",hotKey.toString());
                Platform.runLater(()->{
                    primaryStage.show();
                    primaryStage.toFront();
                    primaryStage.centerOnScreen();
                });
            }
        });
        provider.register(KeyStroke.getKeyStroke("control 4"), new HotKeyListener() {
            public void onHotKey(HotKey hotKey) {

                LoggerFactory.getLogger().debug("{}",hotKey.toString());
                Platform.runLater(()->{
                    primaryStage.hide();
                });
            }
        });
    }

    public void closeStage() {
        LoggerFactory.getLogger().debug("closeStage");
        provider.reset();
        provider.stop();
    }

    public Scene getRootScene() {

        return new Scene(getContentNode(), 1024, 560);
    }

    public Parent getContentNode() {

        VBox vBox = new VBox();
        Button show = new Button("显示");
        Button hide = new Button("隐藏");
        Button toBack = new Button("退回后台");
        Button toFront = new Button("前台");
        vBox.getChildren().addAll(show, hide,toBack,toFront);
        hide.setOnAction((e)->{
            primaryStage.hide();;
        });
        toBack.setOnAction((e)->{
            primaryStage.toBack();
        });
        toFront.setOnAction((e)->{
            primaryStage.toFront();;
        });
        return vBox;
    }

    @Override
    public void onHotKey(HotKey hotKey) {

    }
}
