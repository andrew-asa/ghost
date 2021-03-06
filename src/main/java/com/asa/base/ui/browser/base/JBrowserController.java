package com.asa.base.ui.browser.base;

/**
 * @author andrew_asa
 * @date 2021/10/29.
 */
public interface JBrowserController {

    /**
     * 因此导航
     */
    void hideNav();

    /**
     * 隐藏
     */
    void hide();

    /**
     * 加载url
     * @param url
     */
    void load(String url);

    /**
     * 导出为pdf
     */
    void exportPdf();

    /**
     * 导出为pdf
     * @param url
     */
    void exportPdf(String url);

    /**
     * 前进,越界返回-1
     */
    int forward();

    /**
     * 后退。越界返回-1
     * @return
     */
    int back();

    /**
     * 获取文章标题
     * @return
     */
    String getTitle();

    /**
     * 获取当前地址
     * @return
     */
    String getLocation();
}
