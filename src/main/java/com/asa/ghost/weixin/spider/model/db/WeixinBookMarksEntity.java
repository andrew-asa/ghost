package com.asa.ghost.weixin.spider.model.db;

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
 * 收藏文章书签
 */
@Entity
@Table(name = "weixin_book_marks")
public class WeixinBookMarksEntity {

    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    private String id;


    /**
     * 文章名字
     */
    @Column
    private String name;

    /**
     * 目录id
     */
    @Column
    private String parent;

    @Column
    private String url;

    @Column
    private String tags;

    @Column
    private boolean folder;

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

    public String getTags() {

        return tags;
    }

    public void setTags(String tags) {

        this.tags = tags;
    }

    public boolean isFolder() {

        return folder;
    }

    public void setFolder(boolean folder) {

        this.folder = folder;
    }
}
