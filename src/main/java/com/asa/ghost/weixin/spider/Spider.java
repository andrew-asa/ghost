package com.asa.ghost.weixin.spider;

import com.asa.ghost.weixin.spider.view.MainView;
import com.asa.ghost.weixin.spider.view.SplashScreenCustom;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;

/**
 * @author andrew_asa
 * @date 2021/3/26.
 */
@SpringBootApplication(scanBasePackages = "com.asa.ghost.weixin.spider")
public class Spider extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {

        launch(Spider.class, MainView.class, new SplashScreenCustom(), args);

    }

    public void start(final Stage stage) throws Exception {

        super.start(stage);
        setTray(stage);
        //stage.setOnCloseRequest(e -> {
        //    System.exit(0);
        //});
    }

    private void setTray(Stage stage) {

        try {
            //BufferedImage image = ImageIO.read(new ClassPathResource("com/asa/weixin/spider/img/Jarvis.png").getInputStream());
            //TrayIcon trayIcon = initTray("jarvis", image);
            stage.setOnCloseRequest(event -> {
                //try {
                //    if (SystemTray.isSupported())
                //        if (trayIcon != null)
                //            SystemTray.getSystemTray().remove(trayIcon);
                //} catch (Exception ignored) {
                //}
                System.exit(0);
            });
        } catch (Exception e) {

        }

    }

    private TrayIcon initTray(String appName, BufferedImage bufferedImage) throws Exception {

        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            //BufferedImage bufferedImage = SwingFXUtils.fromFXImage(logoImage, null);
            PopupMenu popupMenu = new PopupMenu();
            java.awt.MenuItem exitItem = new java.awt.MenuItem("退出");
            exitItem.addActionListener(e -> {
                System.exit(0);
            });
            popupMenu.add(exitItem);
            java.awt.MenuItem back = new java.awt.MenuItem("回主页面");
            back.addActionListener(e -> {
                Platform.runLater(() -> {
                    Stage stage = Spider.getStage();
                    if (!Spider.getStage().isShowing()) {
                        stage.show();
                    }
                    stage.toFront();
                });
            });
            popupMenu.add(back);
            if (bufferedImage != null) {
                trayIcon = new TrayIcon(bufferedImage, appName);
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip(appName);
                trayIcon.setPopupMenu(popupMenu);
                tray.add(trayIcon);
            }
        }
        return trayIcon;
    }
}
