package com.asa.weixin.spider;

import com.asa.weixin.spider.view.MainView;
import com.asa.weixin.spider.view.SplashScreenCustom;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author andrew_asa
 * @date 2021/3/26.
 */
@SpringBootApplication
public class Spider extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {

        launch(Spider.class, MainView.class, new SplashScreenCustom(),args);
    }
}
