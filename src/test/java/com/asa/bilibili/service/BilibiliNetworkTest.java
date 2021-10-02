package com.asa.bilibili.service;

import junit.framework.TestCase;

import javax.sound.midi.MidiChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andrew_asa
 * @date 2021/10/2.
 */
public class BilibiliNetworkTest extends TestCase {

    public void testGET() {

        BilibiliNetwork network = new BilibiliNetwork();
        Map g = network.GET("https://api.bilibili.com/x/space/acc/info?mid=77859059", new HashMap());
        System.out.println(g);
    }

    public void testGET2() {

        BilibiliNetwork network = new BilibiliNetwork();
        Map params = new HashMap();
        params.put("mid", 77859059);
        Map g = network.GET("https://api.bilibili.com/x/space/acc/info", params);
        System.out.println(g);
    }

    public void testAddURIVariables() {

        String url = BilibiliNetwork.addURIVariables("https://api.bilibili.com/x/space/acc/info", "mid");
        System.out.println(url);
    }
}