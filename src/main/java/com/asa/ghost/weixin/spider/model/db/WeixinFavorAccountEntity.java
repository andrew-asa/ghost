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
 */
@Entity
@Table(name = "weixin_favor_acounts")
public class WeixinFavorAccountEntity {
    /**
     * 微信号
     */
    @Column private String alias;

    /**
     * id
     */
    @Id
    private String fakeId;

    /**
     * 微信名字
     */
    @Column private String nickName;

    /**
     * 头像url
     */
    @Column private String roundHeadImg;
    @Column private String serviceType;
    @Column private String signature;
    public WeixinFavorAccountEntity() {

    }

    public String getAlias() {

        return alias;
    }

    public void setAlias(String alias) {

        this.alias = alias;
    }

    public String getFakeId() {

        return fakeId;
    }

    public void setFakeId(String fakeId) {

        this.fakeId = fakeId;
    }

    public String getNickName() {

        return nickName;
    }

    public void setNickName(String nickName) {

        this.nickName = nickName;
    }

    public String getRoundHeadImg() {

        return roundHeadImg;
    }

    public void setRoundHeadImg(String roundHeadImg) {

        this.roundHeadImg = roundHeadImg;
    }

    public String getServiceType() {

        return serviceType;
    }

    public void setServiceType(String serviceType) {

        this.serviceType = serviceType;
    }

    public String getSignature() {

        return signature;
    }

    public void setSignature(String signature) {

        this.signature = signature;
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", WeixinFavorAccountEntity.class.getSimpleName() + "[", "]")
                .add("fakeId='" + fakeId + "'")
                .add("nickName='" + nickName + "'")
                .toString();
    }
}
