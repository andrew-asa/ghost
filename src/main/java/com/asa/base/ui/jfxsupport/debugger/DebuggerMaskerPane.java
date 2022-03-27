package com.asa.base.ui.jfxsupport.debugger;

import com.asa.base.ui.control.BaseJControl;
import com.asa.base.ui.jfxsupport.debugger.skin.DebuggerMaskerPaneSkin;
import javafx.scene.control.Skin;

/**
 * @author andrew_asa
 * @date 2021/11/23.
 */
public class DebuggerMaskerPane extends BaseJControl {

    private DebuggerController controller;

    public DebuggerMaskerPane(DebuggerController controller) {
        this.controller = controller;
        getStyleClass().add("debugger-masker-pane");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DebuggerMaskerPaneSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getUserAgentStylesheet(DebuggerMaskerPane.class, "DebuggerMaskerPane.css");
    }

    public DebuggerController getController() {

        return controller;
    }
}
