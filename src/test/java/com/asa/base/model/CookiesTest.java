package com.asa.base.model;

import junit.framework.TestCase;

/**
 * @author andrew_asa
 * @date 2021/10/3.
 */
public class CookiesTest extends TestCase {

    public void testReadFromString() {

        String str = " l=v; bcd=abe; 123=456; 123=789";
        Cookies cs = new Cookies();
        cs.readFromString(str);
        Cookie cookie = cs.getFirst("l");
        Cookie cookie2 = cs.getFirst("123");
        assertEquals(cookie.getValue(), "v");
        assertEquals(cookie2.getValue(), "456");
    }
}