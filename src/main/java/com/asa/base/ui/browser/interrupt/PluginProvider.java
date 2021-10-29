package com.asa.base.ui.browser.interrupt;

import java.util.List;

/**
 * @author andrew_asa
 * @date 2021/7/9.
 */
public interface PluginProvider {

    String getName();

    List<String> getJsPaths();
}
