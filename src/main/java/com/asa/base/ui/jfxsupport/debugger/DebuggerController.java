package com.asa.base.ui.jfxsupport.debugger;

import com.asa.base.log.LoggerFactory;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.controlsfx.control.MaskerPane;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.awt.event.ActionListener;

/**
 * @author andrew_asa
 * @date 2021/11/22.
 */
@Component
public class DebuggerController {

    private Scene scene;

    private Stage stage;

    private StackPane root;

    private boolean debugging = false;

    private DebuggerMaskerPane loginMaskerPane;

    public void debugger() {

        if (debugging) {
            return;
        }
        LoggerFactory.getLogger().debug("debugger");
        Platform.runLater(this::setupDebugger);
    }

    private void setupDebugger() {

        debugging = true;
        loginMaskerPane = new DebuggerMaskerPane(this);
        // 遮罩显示
        loginMaskerPane.setVisible(true);
        root.getChildren().add(loginMaskerPane);
    }

    public void cancelDebugger() {

        if (!debugging) {
            return;
        }
        Platform.runLater(this::removeDebugger);
    }

    private void removeDebugger() {

        debugging = false;
        root.getChildren().remove(loginMaskerPane);
        LoggerFactory.getLogger().debug("cancel debugger");
    }

    public boolean isDebugging() {

        return debugging;
    }

    public void setDebugging(boolean debugging) {

        this.debugging = debugging;
    }

    public void init(Stage stage, Scene scene, StackPane root) {

        this.stage = stage;
        this.scene = scene;
        this.root = root;
    }

    public java.awt.MenuItem createDebuggerMenu(String debuggerLabel, String quitDebuggerLabel) {

        java.awt.MenuItem debugger = new java.awt.MenuItem(debuggerLabel);
        ActionListener debuggerListener = (event) -> {
            if (!isDebugging()) {
                debugger();
                debugger.setLabel(quitDebuggerLabel);
            } else {
                cancelDebugger();
                debugger.setLabel(debuggerLabel);
            }
        };
        debugger.addActionListener(debuggerListener);
        return debugger;
    }

    public Scene getScene() {

        return scene;
    }

    public void setScene(Scene scene) {

        this.scene = scene;
    }

    public Stage getStage() {

        return stage;
    }

    public void setStage(Stage stage) {

        this.stage = stage;
    }

    public StackPane getRoot() {

        return root;
    }

    public void setRoot(StackPane root) {

        this.root = root;
    }
}
