package com.asa.weixin.spider.model;

import java.util.StringJoiner;

/**
 * @author andrew_asa
 * @date 2021/3/28.
 */
public class WeixinArticle {

    private String aid;
    private String album_id;
    //appmsg_album_infos: []
    private String appmsgid;
    private int  checking;

    /**
     * 0-->分享的，或者广告
     * 1-->自己创作的
     * 2-->别人写的
     */
    private int  copyright_type;
    private String cover;
    private long create_time;
    private String digest;
    private int  has_red_packet_cover;
    private int  is_pay_subscribe;
    private int  item_show_type;
    private int itemidx;
    private String link;
    //private String media_duration: "0:00"
    //private String mediaapi_publish_status: 0
    //private String pay_album_info: {appmsg_album_infos: []}
    //private String tagid: []
    private String title;
    private long  update_time;

    public String getAid() {

        return aid;
    }

    public void setAid(String aid) {

        this.aid = aid;
    }

    public String getAlbum_id() {

        return album_id;
    }

    public void setAlbum_id(String album_id) {

        this.album_id = album_id;
    }

    public String getAppmsgid() {

        return appmsgid;
    }

    public void setAppmsgid(String appmsgid) {

        this.appmsgid = appmsgid;
    }

    public int getChecking() {

        return checking;
    }

    public void setChecking(int checking) {

        this.checking = checking;
    }

    public int getCopyright_type() {

        return copyright_type;
    }

    public void setCopyright_type(int copyright_type) {

        this.copyright_type = copyright_type;
    }

    public String getCover() {

        return cover;
    }

    public void setCover(String cover) {

        this.cover = cover;
    }

    public long getCreate_time() {

        return create_time;
    }

    public void setCreate_time(long create_time) {

        this.create_time = create_time;
    }

    public String getDigest() {

        return digest;
    }

    public void setDigest(String digest) {

        this.digest = digest;
    }

    public int getHas_red_packet_cover() {

        return has_red_packet_cover;
    }

    public void setHas_red_packet_cover(int has_red_packet_cover) {

        this.has_red_packet_cover = has_red_packet_cover;
    }

    public int getIs_pay_subscribe() {

        return is_pay_subscribe;
    }

    public void setIs_pay_subscribe(int is_pay_subscribe) {

        this.is_pay_subscribe = is_pay_subscribe;
    }

    public int getItem_show_type() {

        return item_show_type;
    }

    public void setItem_show_type(int item_show_type) {

        this.item_show_type = item_show_type;
    }

    public int getItemidx() {

        return itemidx;
    }

    public void setItemidx(int itemidx) {

        this.itemidx = itemidx;
    }

    public String getLink() {

        return link;
    }

    public void setLink(String link) {

        this.link = link;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public long getUpdate_time() {

        return update_time;
    }

    public void setUpdate_time(long update_time) {

        this.update_time = update_time;
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", WeixinArticle.class.getSimpleName() + "[", "]")
                .add("link='" + link + "'")
                .add("title='" + title + "'")
                .toString();
    }
}
