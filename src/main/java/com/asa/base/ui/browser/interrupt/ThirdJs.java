package com.asa.base.ui.browser.interrupt;

import java.util.Arrays;
import java.util.List;

/**
 * @author andrew_asa
 * @date 2021/7/9.
 * 第三方js
 */

public class ThirdJs implements PluginProvider{

    @Override
    public String getName() {

        return "third";
    }

    @Override
    public List<String> getJsPaths() {

        return Arrays.asList("com/asa/browser/third/jquery-3.6.0.js");
    }
}
