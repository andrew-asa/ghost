package com.asa.utils;

import junit.framework.TestCase;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/10/2.
 */
public class ObjectMapUtilsTest extends TestCase {

    public void testGetMap() {
        Map m1 = new HashMap();
        m1.put("m1abc", 123);
        m1.put("m1cdf", 456);

        Map m2 = new HashMap();
        m2.put("m2abc", 123);
        m2.put("m2cdf", 456);

        m1.put("m1efg",m2);


        Map r = ObjectMapUtils.getMap(m1);
        Assert.assertTrue(MapUtils.containsKey(r,"m1abc"));
        Assert.assertTrue(MapUtils.containsKey(r,"m1cdf"));
        Assert.assertTrue(MapUtils.containsKey(r,"m1efg"));

        Map r2 = ObjectMapUtils.getMap(m1,"m1efg");
        Assert.assertTrue(MapUtils.containsKey(r2,"m2abc"));
        Assert.assertTrue(MapUtils.containsKey(r2,"m2cdf"));

        Map r3 = ObjectMapUtils.getMap(m1, "m1efg", "abc");
        Assert.assertNull(r3);

        Map r4 = ObjectMapUtils.getMap(m1, "m1efg1", "abc");
        Assert.assertNull(r4);
    }

    public void testGetString() {
        Map m1 = new HashMap();
        m1.put("m1abc", "123");
        m1.put("m1cdf", "456");

        Map m2 = new HashMap();
        m2.put("m2abc", "123");
        m2.put("m2cdf", "456");

        m1.put("m1efg",m2);

        Assert.assertEquals(ObjectMapUtils.getString(m1,"m1abc"),"123");
        Assert.assertEquals(ObjectMapUtils.getString(m1,"m1cdf"),"456");
        Assert.assertEquals(ObjectMapUtils.getString(m1,"m1efg","m2abc"),"123");
        Assert.assertEquals(ObjectMapUtils.getString(m1,"m1efg","m2cdf"),"456");
        Assert.assertNull(ObjectMapUtils.getString(m1,"m1efg","m2cdf1"));
        Assert.assertNull(ObjectMapUtils.getString(m1,"m1efg2","m2cdf1"));
        Assert.assertNull(ObjectMapUtils.getString(m1,"m1efg2","m2cdf1","aaa"));
    }

}