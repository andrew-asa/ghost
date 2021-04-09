package com.asa.weixin.spider.model;


import com.asa.utils.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author andrew_asa
 * @date 2021/4/9.
 */
public class FavorItem {

    /**
     * 收藏选项内容是子节点还是根节点
     */
    public  enum FavorItemType{

        PARENT,
        CHILDREN;

        @Override
        public String toString() {

            return new StringJoiner(", ", FavorItemType.class.getSimpleName() + "[", "]")
                    .toString();
        }
    }

    private FavorItemType type = FavorItemType.CHILDREN;

    private String name;
    private String url;
    private List<FavorItem> children;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public List<FavorItem> getChildren() {

        return children;
    }

    public void setChildren(List<FavorItem> children) {

        this.children = children;
    }

    public FavorItem appendChild(FavorItem child) {
        Validate.notNull(child);
        ensureChildNodes();
        children.add(child);
        return this;
    }

    protected List<FavorItem> ensureChildNodes() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public FavorItemType getType() {

        return type;
    }

    public void setType(FavorItemType type) {

        this.type = type;
    }

    public boolean isChildren() {

        return FavorItemType.CHILDREN.equals(type);
    }

    public boolean isParent() {
        return FavorItemType.PARENT.equals(type);
    }

    public void setIsChildren() {
        setType(FavorItemType.CHILDREN);
    }

    public void setIsParent() {
        setType(FavorItemType.PARENT);
    }
}
