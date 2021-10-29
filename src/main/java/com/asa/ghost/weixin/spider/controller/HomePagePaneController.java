package com.asa.ghost.weixin.spider.controller;

import com.asa.base.enent.EventDispatcher;
import com.asa.base.enent.Listener;
import com.asa.base.log.LoggerFactory;
import com.asa.base.utils.ListUtils;
import com.asa.base.utils.StringUtils;
import com.asa.ghost.weixin.spider.Spider;
import com.asa.ghost.weixin.spider.view.ArticleListPaneView;
import com.asa.ghost.weixin.spider.view.FavorAccountsView;
import com.asa.ghost.weixin.spider.view.SearchPresentView;
import com.asa.ghost.weixin.spider.view.SubViewContent;
import com.asa.ghost.weixin.spider.view.WeixinPublicAccountSearchView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@FXMLController
public class HomePagePaneController implements Initializable {

    @FXML
    private BorderPane centerPanel;

    @FXML
    private HBox settingBox;

    @FXML
    private HBox searchBox;

    @FXML
    private Pane clearSearch;

    // 搜索框
    @FXML
    private TextField searchField;

    /**
     * 左边栏
     */
    @FXML
    private VBox sideBar;

    @Autowired
    private List<SubViewContent> subViews;

    @Autowired
    private List<SearchPresentView> searchPresentViews;

    /**
     * 当前搜索标签
     */
    public String currentSearchTag = WeixinPublicAccountSearchView.NAME;

    private CountDownLatch viewLoadedLatch;

    private String lastView = StringUtils.EMPTY;

    private double expandedHeight = 50;

    private double collapsedHeight = 0;


    /**
     * 搜索结构弹窗框
     */
    private Stage searchPopup;

    private VBox searchPopupView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        resetLatch();
        installListener();
        settingSearchBox();
        createSearchPopup();
        loadSubView(FavorAccountsView.NAME);
        preLoadSubViews();
    }

    @FXML
    private void slideSideBar(Event e) {

        ((HBox) e.getSource()).requestFocus();
    }


    /**
     * 创建搜索弹出框
     */
    private void createSearchPopup() {

        try {
            Stage stage = Spider.getStage();
            searchPopupView = new VBox();
            Stage popup = new Stage();
            popup.setScene(new Scene(searchPopupView));
            popup.initStyle(StageStyle.UNDECORATED);
            popup.initOwner(stage);
            searchHideAnimation.setOnFinished(x -> {popup.hide();});
            searchShowAnimation.setOnFinished(x -> {
                LoggerFactory.getLogger().debug(this.getClass(),"searchField request focus");
                searchField.requestFocus();
            });
            popup.show();
            popup.hide();
            searchPopup = popup;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void settingSearchBox() {

        settingSearchField();
        settingClearSearchButton();
    }

    private void settingClearSearchButton() {

        clearSearch.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (clearSearch.isVisible()) {
                    hideClearSearch();
                    clearSearchText();
                    hideSearchPop();
                }
            }
        });
    }

    private void settingSearchField() {

        searchField.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {

                if (newPropertyValue) {
                    System.out.println("Text field on focus");
                    //showSearchPop();
                } else {
                    System.out.println("Text field out focus");
                    //hideSearchPop();
                }
            }
        });
        searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {

                if (event.getCode() == KeyCode.ENTER) {
                    String text = searchField.getText();
                    if (StringUtils.isNotEmpty(text)) {
                        LoggerFactory.getLogger().debug(this.getClass(),"search {}", text);
                        showSearchPop();
                        EventDispatcher.asyncFire(SearchEvent.START_SEARCH,new SearchItem(getCurrentSearchTag(),text));
                    }
                }
            }
        });
        searchField.textProperty().addListener((observable, oldText, newText) -> {
            String text = newText.trim();
            if (StringUtils.isEmpty(text)) {
                hideClearSearch();
                hideSearchPop();
            } else {
                showClearSearch();
            }
        });
    }

    private void clearSearchText() {

        searchField.clear();
    }


    private Animation searchShowAnimation = new Transition() {

        {
            setCycleDuration(Duration.millis(250));
            setInterpolator(Interpolator.EASE_BOTH);
        }

        protected void interpolate(double frac) {

            searchPopup.setOpacity(frac);
        }
    };

    private Animation searchHideAnimation = new Transition() {

        {
            setCycleDuration(Duration.millis(250));
            setInterpolator(Interpolator.EASE_BOTH);
        }

        @Autowired
        protected void interpolate(double frac) {

            searchPopup.setOpacity(1.0 - frac);
        }
    };

    public void hideSearchPop() {
        EventDispatcher.asyncFire(SearchEvent.STOP_SEARCH,new SearchItem(getCurrentSearchTag(),""));
        searchHideAnimation.play();
    }

    public void showSearchPop() {

        if (!searchPopup.isShowing()) {
            searchPopupView.getChildren().removeAll();
            Stage stage = Spider.getStage();
            searchPopup.setX(stage.getX());
            searchPopup.setY(stage.getY() + 80);
            searchPopup.show();
            List<SearchPresentView> searchPresentView = getSearchPresentView(getCurrentSearchTag());
            EventDispatcher.fire(MainPanelEvent.SEARCH_RESULT_PANE_SHOW, getCurrentSearchTag());
            if (ListUtils.isNotEmpty(searchPresentView)) {
                searchPresentView.forEach((v)->{
                    Parent view = v.getView();
                    ListUtils.putIfAbsent(searchPopupView.getChildren(),view);
                });
            }
            searchShowAnimation.play();
        }
    }

    public String getCurrentSearchTag() {

        return currentSearchTag;
    }

    public List<SearchPresentView> getSearchPresentView(String searchTag) {

        return searchPresentViews.stream()
                .filter((sb) -> {
                    return sb.accept(searchTag);
                }).collect(Collectors.toList());
    }

    public void showClearSearch() {

        clearSearch.setVisible(true);
    }

    public void hideClearSearch() {

        clearSearch.setVisible(false);
    }

    void resetLatch() {

        viewLoadedLatch = new CountDownLatch(1);
    }

    private void installListener() {


        EventDispatcher.listen(HomePageSubPanelEvent.REQUIRE_INSTALL, new Listener<String>() {

            @Override
            public void on(com.asa.base.enent.Event event, String param) {
                hideSearchPop();
                loadSubView(param);
            }
        });
        //EventDispatcher.listen(MainPanelEvent.BEFORE_INSTALL, new Listener<String>() {
        //
        //    @Override
        //    public void on(com.asa.base.enent.Event event, String param) {
        //
        //        hideSearchPop();
        //    }
        //});
    }


    /**
     * 加载子页面
     *
     * @param viewName
     */
    public void loadSubView(String viewName) {

        if (StringUtils.equals(viewName, lastView)) {
            return;
        }
        SubViewContent subViewContent = getSubView(viewName);
        if (subViewContent == null) {
            return;
        }
        String tl = lastView;
        lastView = viewName;
        Node view = subViewContent.getView();
        Platform.runLater(() -> {
            EventDispatcher.fire(HomePageSubPanelEvent.UNINSTALL, tl);
            centerPanel.setCenter(view);
            centerPanel.setVisible(true);
            EventDispatcher.fire(HomePageSubPanelEvent.INSTALL, viewName);
        });
    }


    private void preLoadSubViews() {
        subViews.forEach(e -> e.getView());
    }

    public SubViewContent getSubView(String viewName) {

        Optional<SubViewContent> sbv = subViews.stream()
                .filter((sb) -> {
                    return sb.accept(viewName);
                })
                .findFirst();
        return sbv.isPresent() ? sbv.get() : null;
    }

    @FXML
    private void selectView(Event e) {

        HBox eventSource = ((HBox) e.getSource());
        sideBar.getChildren().forEach(n -> {
            n.getStyleClass().removeAll("sideBarItemSelected");
            n.getStyleClass().setAll("sideBarItem");
        });
        eventSource.getStyleClass().setAll("sideBarItemSelected");
        eventSource.requestFocus();
        loadSubView(eventSource.getId());
    }
}
