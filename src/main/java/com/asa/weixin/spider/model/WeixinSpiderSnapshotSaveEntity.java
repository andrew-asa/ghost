package com.asa.weixin.spider.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author andrew_asa
 * @date 2020/12/30.
 * 快照
 */
@Entity
@Table(name = "weixin_spider_snapshot_save")
public class WeixinSpiderSnapshotSaveEntity {
    @Id
    private String key;

    @Column(columnDefinition = "TEXT")
    private String value;

    public WeixinSpiderSnapshotSaveEntity() {

    }

    public WeixinSpiderSnapshotSaveEntity(String key, String value) {

        this.key = key;
        this.value = value;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public String getValue() {

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }
}
