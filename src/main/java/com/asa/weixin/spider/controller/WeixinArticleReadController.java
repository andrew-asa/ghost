package com.asa.weixin.spider.controller;

import com.asa.base.enent.Event;
import com.asa.base.enent.EventDispatcher;
import com.asa.base.enent.Listener;
import com.asa.browser.JBrowser;
import com.asa.log.LoggerFactory;
import com.asa.utils.ListUtils;
import com.asa.utils.StringUtils;
import com.asa.weixin.spider.Spider;
import com.asa.weixin.spider.model.WeixinArticle;
import com.asa.weixin.spider.service.WeixinFavorArticleService;
import com.asa.weixin.spider.service.pdf.HtmlToPdfService;
import com.asa.weixin.spider.ui.component.Toast;
import com.asa.weixin.spider.view.ArticleListPaneView;
import com.asa.weixin.spider.view.WeixinArticleReadView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;

//import org.controlsfx.control.PopOver;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author andrew_asa
 * @date 2021/3/26.
 * 阅读公众号文章
 */
@FXMLController
public class WeixinArticleReadController implements Initializable {

    @FXML
    private JBrowser browser;

    @FXML
    private Button back;

    @FXML
    private Button forward;

    @FXML
    private Button pageHome;

    @FXML
    private Button pdfConverter;

    @FXML
    private Button addToFavor;

    @FXML
    private SVGPath favorImg;

    //private WebEngine webEngine;

    @Autowired
    private WeixinFavorArticleService favorArticleService;

    private WeixinArticle weixinArticle;

    private String folder;
    private String title;

    public enum WeixinArticleReadEvent implements Event<WeixinArticle> {

        /**
         * 请求加载url
         */
        REQUIRE_LOAD,
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LoggerFactory.getLogger().debug(this.getClass(),"WeixinArticleReadController initialize");
        initListener();
        initWebEngine();
        initButtonAction();
    }

    private void initButtonAction() {

        pageHome.setOnMouseClicked(e -> pageHome());
        pageHome.setTooltip(new Tooltip("返回文章列表"));

        back.setOnMouseClicked(e -> back());
        back.setTooltip(new Tooltip("后退"));

        forward.setOnMouseClicked(e -> forward());
        forward.setTooltip(new Tooltip("前进"));

        pdfConverter.setOnMouseClicked(e -> pdfConverter());
        pdfConverter.setTooltip(new Tooltip("导出为pdf"));

        initAddToFavor();
    }


    public void pdfConverter() {

        browser.exportPdf();
    }



    public void forward() {
        browser.forward();
        //final WebHistory history = webEngine.getHistory();
        //ObservableList<WebHistory.Entry> entryList = history.getEntries();
        //int currentIndex = history.getCurrentIndex();
        //if (currentIndex + 1 < ListUtils.length(entryList)) {
        //    history.go(1);
        //    LoggerFactory.getLogger().debug(this.getClass(),"forward {}", entryList.get(currentIndex < entryList.size() - 1 ? currentIndex + 1 : currentIndex).getUrl());
        //}
    }

    public void back() {

        //final WebHistory history = webEngine.getHistory();
        //ObservableList<WebHistory.Entry> entryList = history.getEntries();
        //int currentIndex = history.getCurrentIndex();
        //if (currentIndex > 0) {
        //    history.go(-1);
        //    //LoggerFactory.getLogger().debug("back {}",entryList.get(currentIndex>0?currentIndex-1:currentIndex).getUrl());
        //} else {
        //    pageHome();
        //}
        int index = browser.back();
        if (index < 0) {
            pageHome();
        }
    }

    public void pageHome() {

        EventDispatcher.fire(HomePageSubPanelEvent.REQUIRE_INSTALL, ArticleListPaneView.NAME);
    }

    private void initWebEngine() {

        //webEngine = webContainer.getEngine();
        //webEngine.getLoadWorker().stateProperty()
        //        .addListener(new ChangeListener<State>() {
        //            @Override
        //            public void changed(ObservableValue ov, State oldState, State newState) {
        //
        //                if (newState == Worker.State.SUCCEEDED) {
        //                    stage.setTitle(webEngine.getLocation());
        //                }
        //
        //            }
        //        });
    }

    private void initListener() {

        EventDispatcher.listen(WeixinArticleReadEvent.REQUIRE_LOAD, new Listener<WeixinArticle>() {

            @Override
            public void on(Event event, WeixinArticle param) {

                EventDispatcher.fire(HomePageSubPanelEvent.REQUIRE_INSTALL, WeixinArticleReadView.NAME);
                weixinArticle = param;
                browser.load(param.getLink());
            }
        });
    }

    private void initAddToFavor() {

        addToFavor.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

            Label bookmarksLabel = new Label("添加书签");
            VBox popUpContent = new VBox();

            popUpContent.setMinSize(300, 250);
            popUpContent.setSpacing(5);
            popUpContent.setPadding(new Insets(5, 5, 5, 5));
            Label nameLabel = new Label("名字");
            JFXTextField markNameText = new JFXTextField();

            markNameText.setText(browser.getTitle());
            Label folderLabel = new Label("文件夹");

            List<String> folders = favorArticleService.getFolders();
            ObservableList<String> options = FXCollections.observableArrayList(folders);
            JFXComboBox<String> markFolderList = new JFXComboBox<>(options);
            markFolderList.setMinWidth(300);
            markFolderList.getSelectionModel().select(0);

            JFXButton cancelPopup = new JFXButton("取消");
            cancelPopup.setMinSize(100, 30);

            JFXButton newFolderMarkFolder = new JFXButton("新建文件夹");
            newFolderMarkFolder.setMinSize(100, 30);

            JFXButton saveMark = new JFXButton("保存");
            saveMark.setMinSize(100, 30);

            HBox hbox = new HBox();
            hbox.setSpacing(5);
            hbox.getChildren().addAll(cancelPopup, newFolderMarkFolder, saveMark);
            // markFolderList.setVisibleRowCount(0);

            bookmarksLabel.setId("bookmarkLabel");
            nameLabel.setId("bookmarkLabel");
            markFolderList.setId("bookmarkLabel");
            folderLabel.setId("bookmarkLabel");
            markFolderList.setId("bookmarkLabel");

            VBox.setMargin(bookmarksLabel, new Insets(5, 5, 5, 5));
            VBox.setMargin(nameLabel, new Insets(5, 5, 5, 5));
            VBox.setMargin(markNameText, new Insets(5, 5, 5, 5));
            VBox.setMargin(folderLabel, new Insets(5, 5, 5, 5));
            VBox.setMargin(markFolderList, new Insets(5, 5, 5, 5));

            popUpContent.getChildren().addAll(bookmarksLabel, nameLabel, markNameText, folderLabel, markFolderList,
                                              hbox);

            PopOver popOver = new PopOver(new JFXButton("Yes"));

            // popOver.getRoot().getStylesheets().add("Yes");
            // popOver.setDetachable(true);
            popOver.setCornerRadius(4);
            popOver.setContentNode(popUpContent);
            // popOver.setMinSize(400, 400);
            popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_LEFT);
            popOver.show(addToFavor);



            cancelPopup.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler -> {
                popOver.hide();
            });

            saveMark.addEventHandler(MouseEvent.MOUSE_CLICKED, (s) -> {

                if (folder == null) {
                    folder = markFolderList.getItems().get(0);
                }
                title = markNameText.getText();
                if (title == null) {
                    title = browser.getTitle();
                }
                favorArticleService.addToFavor(browser.getLocation(), folder,title);
                popOver.hide();
            });
            newFolderMarkFolder.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {


                //TextInputDialog dialog = new TextInputDialog("All Bookmarks");
                //dialog.setTitle("新建文件夹");
                //dialog.setHeaderText("新建文件夹");
                //dialog.setContentText("输入文件夹名字:");
                //Optional<String> result = dialog.showAndWait();
                //
                //result.ifPresent(name ->{
                //    if(StringUtils.isEmpty(title)){
                //        title = webEngine.getTitle();
                //    }
                //    if(StringUtils.isNotEmpty(name)){
                //        folder = name;
                //        options.add(folder);
                //        favorArticleService.addToFavor(webEngine.getLocation(), folder,title);
                //    }else{
                //        Toast.makeText("文件夹输入为空", Toast.DURATION_SHORT).show(addToFavor, Side.RIGHT, 0, 0);
                //    }
                //});
            });
        });
    }
}
