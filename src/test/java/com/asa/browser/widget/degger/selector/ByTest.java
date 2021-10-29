package com.asa.browser.widget.degger.selector;

import com.asa.base.ui.browser.widget.degger.selector.By;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * @author andrew_asa
 * @date 2021/7/7.
 */
public class ByTest extends TestCase {

    public void testBuildScript() {

        Assert.assertEquals(By.id("id").buildScript("click()"), "$(\"#id\").click()");
        Assert.assertEquals(By.className("class").buildScript("click()"), "$(\".class\").click()");
    }
}