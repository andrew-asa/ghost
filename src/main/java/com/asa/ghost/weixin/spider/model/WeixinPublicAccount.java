package com.asa.ghost.weixin.spider.model;

import java.util.StringJoiner;

/**
 * @author andrew_asa
 * @date 2021/3/27.
 */
public class WeixinPublicAccount {

    /**
     * 微信号
     */
    private String alias;

    /**
     * id
     */
    private String fakeId;

    /**
     * 微信名字
     */
    private String nickName;

    /**
     * 头像
     */
    private String roundHeadImg;
    private String serviceType;
    private String signature;

    public static class ServiceType{

        public static String showName(String serviceType) {
            switch (serviceType){
                case "1":
                    return "订阅号";
                default:
                    return "未知";
            }
        }
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

    public static WeixinPublicAccount create(String nackName, String alias, String roundHeadImg, String serviceType) {
        WeixinPublicAccount r = new WeixinPublicAccount();
        r.setNickName(nackName);
        r.setAlias(alias);
        r.setRoundHeadImg(roundHeadImg);
        r.setServiceType(serviceType);
        return  r;
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", WeixinPublicAccount.class.getSimpleName() + "[", "]")
                .add("alias='" + alias + "'")
                .add("fakeId='" + fakeId + "'")
                .add("nickName='" + nickName + "'")
                .add("roundHeadImg='" + roundHeadImg + "'")
                .add("serviceType='" + serviceType + "'")
                .add("signature='" + signature + "'")
                .toString();
    }
}
