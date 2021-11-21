package com.asa.ghost.weixin.spider.view;

import com.asa.base.log.LoggerFactory;
import com.asa.base.ui.jfxsupport.SplashScreen;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * @author andrew_asa
 * @date 2020/11/26.
 */
public class SplashScreenCustom extends SplashScreen {

    public static final String FXML = "/com/asa/ghost/weixin/spider/view/";


    /**
     * todo 用spring boot 进行加载
     * @return
     */
    @Override
    public Parent getParent() {
        // 残留
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(FXML + "SplashScreen.fxml"));
        VBox view = null;
        try {
            view = loader.load();
        } catch (IOException e) {
            LoggerFactory.getLogger().error("can not load SplashScreen.fxml");
            return null;
        }
        return view;
    }

}
