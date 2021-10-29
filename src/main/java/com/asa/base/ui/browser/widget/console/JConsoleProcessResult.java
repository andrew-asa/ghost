package com.asa.base.ui.browser.widget.console;

/**
 * @author andrew_asa
 * @date 2021/7/5.
 */
public class JConsoleProcessResult {
    private String msg;
    public  enum Level{
        NORMAL,
        ERROR,
    }

    public JConsoleProcessResult(String msg) {

        this.msg = msg;
    }

    public JConsoleProcessResult(String msg, Level leve) {

        this.msg = msg;
        this.leve = leve;
    }

    private JConsoleProcessResult.Level leve = Level.NORMAL;

    public static JConsoleProcessResult build(String msg) {

        return new JConsoleProcessResult(msg);
    }

    public static JConsoleProcessResult build(String msg,Level level) {

        return new JConsoleProcessResult(msg,level);
    }


    public String getMsg() {

        return msg;
    }

    public void setMsg(String msg) {

        this.msg = msg;
    }

    public Level getLeve() {

        return leve;
    }

    public void setLeve(Level leve) {

        this.leve = leve;
    }
}
