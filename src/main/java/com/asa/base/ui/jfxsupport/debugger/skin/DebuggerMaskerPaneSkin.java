package com.asa.base.ui.jfxsupport.debugger.skin;

import com.asa.base.ui.jfxsupport.debugger.DebuggerController;
import com.asa.base.ui.jfxsupport.debugger.DebuggerMaskerPane;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.MaskerPane;

/**
 * @author andrew_asa
 * @date 2021/11/23.
 */
public class DebuggerMaskerPaneSkin extends SkinBase<DebuggerMaskerPane> {

    public DebuggerMaskerPaneSkin(DebuggerMaskerPane control) {

        super(control);
        getChildren().add(createMasker(control));
    }

    private Node createMasker(DebuggerMaskerPane maskerPane) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10.0);
        vBox.getStyleClass().add("masker-center");
        DebuggerController controller = maskerPane.getController();
        Scene scene = controller.getScene();
        //Parent p = scene.getRoot();

        //Stage stage = controller.getStage();
        //
        //vBox.setPrefWidth(stage.getWidth());
        //vBox.setPrefHeight(stage.getHeight());
        //vBox.getChildren().add(createLabel());
        //vBox.getChildren().add(createProgressIndicator());

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(vBox);

        StackPane glass = new StackPane();
        glass.setAlignment(Pos.CENTER);
        glass.getStyleClass().add("masker-glass"); //$NON-NLS-1$
        glass.getChildren().add(hBox);
        hBox.setLayoutX(100);
        hBox.setLayoutY(200);
        return vBox;
    }

    private Label createLabel() {
        Label text = new Label("detest");
        text.getStyleClass().add("masker-text"); //$NON-NLS-1$
        return text;
    }
}
