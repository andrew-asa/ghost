package com.asa.bilibili.data;

/**
 * @author andrew_asa
 * @date 2021/10/2.
 */
public class Credential {

    private String SESSDATA;
    private String buvid3;
    private String bili_jct;

    public String getSESSDATA() {

        return SESSDATA;
    }

    public void setSESSDATA(String SESSDATA) {

        this.SESSDATA = SESSDATA;
    }

    public String getBuvid3() {

        return buvid3;
    }

    public void setBuvid3(String buvid3) {

        this.buvid3 = buvid3;
    }

    public String getBili_jct() {

        return bili_jct;
    }

    public void setBili_jct(String bili_jct) {

        this.bili_jct = bili_jct;
    }
}
