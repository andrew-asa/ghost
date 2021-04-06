package com.asa.weixin.spider.model.db;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.StringJoiner;

/**
 * @author andrew_asa
 * @date 2020/11/26.
 * 收藏文章
 */
@Entity
@Table(name = "favor_article")
public class FavorArticleEntity {

    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    private String id;


    /**
     * 微信号
     */
    @Column
    private String name;

    /**
     * 微信名字
     */
    @Column
    private String parent;

    @Column
    private String url;

    @Column
    private String tags;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getParent() {

        return parent;
    }

    public void setParent(String parent) {

        this.parent = parent;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }
}
