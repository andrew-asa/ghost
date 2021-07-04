package com.asa.browser;

import com.asa.log.LoggerFactory;
import javafx.scene.layout.BorderPane;

/**
 * @author andrew_asa
 * @date 2021/7/4.
 */
public class JBrowser extends BorderPane implements JBrowserControl {


    /**
     * JBrowserControl
     */

    @Override
    public void hideNav() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void load(String url) {

        LoggerFactory.getLogger().debug(this.getClass(),"load url {}", url);
    }

    @Override
    public void exportPdf() {

    }

    @Override
    public void exportPdf(String url) {

    }
}
