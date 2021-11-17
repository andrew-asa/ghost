package com.asa.ghost.base.ui.tree;

import java.util.StringJoiner;

/**
 * @author andrew_asa
 * @date 2021/11/1.
 * 书签树
 */
public class JBookMarkTree {

    /**
     * id
     */
    private String id;

    /**
     * 名字
     */
    private String name;

    /**
     * 是否是父类
     */
    private boolean parent;

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

    public boolean isParent() {

        return parent;
    }

    public void setParent(boolean parent) {

        this.parent = parent;
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", JBookMarkTree.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("parent=" + parent)
                .toString();
    }
}
