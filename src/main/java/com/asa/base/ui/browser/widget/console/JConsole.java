package com.asa.base.ui.browser.widget.console;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


/**
 * @author andrew_asa
 * @date 2021/7/5.
 * 控制台
 */
public class JConsole extends BorderPane {

    private JConsoleProcess process;

    private VBox box ;
    public JConsole(JConsoleProcess process) {

        this.process = process;
        this.box = new VBox();
        setCenter(box);
    }

    private Node createInputNode() {
        return null;
    }

    private Node createOutput(JConsoleProcessResult result) {
        return null;
    }
}
