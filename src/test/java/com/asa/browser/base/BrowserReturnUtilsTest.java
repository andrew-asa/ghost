package com.asa.browser.base;

import junit.framework.TestCase;
import org.junit.Assert;

/**
 * @author andrew_asa
 * @date 2021/7/8.
 */
public class BrowserReturnUtilsTest extends TestCase {

    public void testToBoolean() {

    }

    public void testTestToBoolean() {

        Assert.assertTrue(BrowserReturnUtils.toBoolean(true));
        Assert.assertTrue(BrowserReturnUtils.toBoolean("true"));
        Assert.assertFalse(BrowserReturnUtils.toBoolean("abc"));
        Assert.assertFalse(BrowserReturnUtils.toBoolean("false"));
        Assert.assertFalse(BrowserReturnUtils.toBoolean("no"));
    }
}