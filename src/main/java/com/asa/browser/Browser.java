package com.asa.browser;

import com.asa.browser.base.JBrowserDebugger;
import com.asa.browser.base.JBrowserWidget;
import com.asa.browser.base.JBrowserController;
import com.asa.browser.interrupt.BrowserInterrupt;
import com.asa.browser.base.JConsoleWebEngineProcess;
import com.asa.browser.widget.console.JConsole;
import com.asa.browser.widget.degger.JBrowserDebuggerImpl;
import com.asa.base.log.LoggerFactory;
import com.asa.base.utils.ListUtils;
import com.asa.base.utils.StringUtils;
import com.asa.weixin.spider.Spider;
import javafx.collections.ObservableList;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

/**
 * @author andrew_asa
 * @date 2021/7/4.
 */
public class Browser extends BorderPane implements JBrowserController, JBrowserWidget {


    private WebView webView;

    private WebEngine webEngine;

    private BrowserInterrupt interrupt;

    private JBrowserDebugger debugger;

    private JConsole console;

    /**
     * creates empty tool bar
     */
    public Browser() {

        initialize();
    }

    public void initialize() {

        webView = new WebView();
        setCenter(webView);
        webEngine = webView.getEngine();
        initInterrupt();
        initDebugger();
        customBrowser();
    }

    /**
     * 插入自己定制的js
     */
    private void initInterrupt() {

        this.interrupt = new BrowserInterrupt(webEngine);
        interrupt.addDefaultInterruptJs();
    }

    private void initDebugger() {

        this.debugger = new JBrowserDebuggerImpl(webEngine);
    }

    private void customBrowser() {
        customConsole();
    }

    private void customConsole() {
        console = new JConsole(new JConsoleWebEngineProcess(webEngine));
    }


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

        webEngine.load(url);
        LoggerFactory.getLogger().debug(this.getClass(), "load url {}", url);
    }

    @Override
    public void exportPdf() {

        LoggerFactory.getLogger().debug(this.getClass(), "pdfConverter");
        try {
            Printer printer = findPdfWriterPrint();
            if (printer == null) {
                //Toast.makeText("请先安装pdfwriterformac").showOnRight(pdfConverter);
            } else {
                PrinterJob job = PrinterJob.createPrinterJob(printer);
                //job.showPageSetupDialog(Spider.getStage());
                job.showPrintDialog(Spider.getStage());
                job.getJobSettings().setJobName(getTitle());
                //job.;
                if (job != null) {
                    webEngine.print(job);
                    job.endJob();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Printer findPdfWriterPrint() {

        Printer[] printers = Printer.getAllPrinters().toArray(new Printer[0]);
        if (printers != null) {
            for (Printer printer : printers) {
                if (StringUtils.equalsIgnoreCase(printer.getName(), "pdfwriter")) {
                    return printer;
                }
            }
        }
        return null;
    }

    @Override
    public void exportPdf(String url) {

    }

    public int forward() {

        final WebHistory history = webEngine.getHistory();
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();
        if (currentIndex + 1 < ListUtils.length(entryList)) {
            history.go(1);
            LoggerFactory.getLogger().debug(this.getClass(), "forward {}", entryList.get(currentIndex < entryList.size() - 1 ? currentIndex + 1 : currentIndex).getUrl());
            return currentIndex++;
        }
        return -1;
    }

    public int back() {

        final WebHistory history = webEngine.getHistory();
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();
        if (currentIndex > 0) {
            history.go(-1);
            return currentIndex--;
        }
        return -1;
    }

    @Override
    public String getTitle() {

        return webEngine.getTitle();
    }

    @Override
    public String getLocation() {

        return webEngine.getLocation();
    }

    @Override
    public JBrowserDebugger getDebugger() {

        return debugger;
    }
}
