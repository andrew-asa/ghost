package com.asa.weixin.spider.service;

import com.asa.utils.ListUtils;
import com.asa.weixin.spider.model.WeixinPublicAccount;
import com.asa.weixin.spider.model.db.WeixinFavorAccountEntity;
import com.asa.weixin.spider.model.db.WeixinFavorAccountsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andrew_asa
 * @date 2021/3/29.
 */
@Component
public class WeixinFavorAccountsService {

    @Autowired
    private WeixinFavorAccountsDao favorAccountsDao;

    public List<WeixinPublicAccount> getAllFavorAccounts() {

        List<WeixinPublicAccount> accounts = new ArrayList<>();
        List<WeixinFavorAccountEntity>  accountEntities = favorAccountsDao.findAll();
        if (ListUtils.isNotEmpty(accountEntities)) {
            accountEntities.forEach(entity -> {
                accounts.add(entityToAccount(entity));
            });
        }
        return accounts;
    }

    /**
     * 收藏公众号
     * @param account
     */
    public void addToFavor(WeixinPublicAccount account) {

        if (account != null) {
            WeixinFavorAccountEntity entity =  accountToEntity(account);
            favorAccountsDao.save(entity);
        }
    }

    /**
     * 移除收藏
     * @param account
     */
    public void removeFavor(WeixinPublicAccount account) {
        favorAccountsDao.deleteById(account.getFakeId());
    }

    /**
     * 是否是收藏的公众号
     * @param account
     * @return
     */
    public boolean isFavorAccount(WeixinPublicAccount account) {
        return favorAccountsDao.existsById(account.getFakeId());
    }

    public WeixinPublicAccount entityToAccount(WeixinFavorAccountEntity entity) {

        WeixinPublicAccount account = new WeixinPublicAccount();
        if (entity != null) {
            account.setSignature(entity.getSignature());
            account.setServiceType(entity.getServiceType());
            account.setRoundHeadImg(entity.getRoundHeadImg());
            account.setNickName(entity.getNickName());
            account.setFakeId(entity.getFakeId());
            account.setAlias(entity.getAlias());
        }
        return account;
    }

    public WeixinFavorAccountEntity accountToEntity(WeixinPublicAccount account) {

        WeixinFavorAccountEntity entity = new WeixinFavorAccountEntity();
        if (account != null) {
            entity.setSignature(account.getSignature());
            entity.setServiceType(account.getServiceType());
            entity.setRoundHeadImg(account.getRoundHeadImg());
            entity.setNickName(account.getNickName());
            entity.setFakeId(account.getFakeId());
            entity.setAlias(account.getAlias());
        }
        return entity;
    }
}
