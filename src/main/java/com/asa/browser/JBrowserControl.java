package com.asa.browser;

/**
 * @author andrew_asa
 * @date 2021/7/4.
 * 浏览器功能
 */
public interface JBrowserControl {

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
}
