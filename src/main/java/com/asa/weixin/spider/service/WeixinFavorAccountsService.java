package com.asa.weixin.spider.service;

import com.asa.utils.ListUtils;
import com.asa.utils.StringUtils;
import com.asa.weixin.spider.model.WeixinPublicAccount;
import com.asa.weixin.spider.model.WeixinSpiderSnapshotSaveDao;
import com.asa.weixin.spider.model.WeixinSpiderSnapshotSaveEntity;
import com.asa.weixin.spider.model.db.WeixinFavorAccountEntity;
import com.asa.weixin.spider.model.db.WeixinFavorAccountsDao;
import com.asa.weixin.spider.utils.SnapshotSaveConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author andrew_asa
 * @date 2021/3/29.
 */
@Component
public class WeixinFavorAccountsService {

    @Autowired
    private WeixinFavorAccountsDao favorAccountsDao;

    @Autowired
    private WeixinSpiderSnapshotSaveDao spiderSnapshotSaveDao;

    /**
     * 更新收藏号顺序
     * @param accounts
     */
    public void updateFavourSeqAccount(List<WeixinPublicAccount> accounts) {

        if (ListUtils.isNotEmpty(accounts)) {
            List<String> ids = accounts.stream()
                    .map(WeixinPublicAccount::getFakeId)
                    .collect(Collectors.toList());
            String ks = StringUtils.join(ids, SnapshotSaveConstant.FAVOR_ACCOUNT_LIST_SEPARATOR);
            WeixinSpiderSnapshotSaveEntity se = new WeixinSpiderSnapshotSaveEntity(SnapshotSaveConstant.FAVOR_ACCOUNT, ks);
            spiderSnapshotSaveDao.save(se);
        }
    }

    public List<String> getFavorSeqAccountFakeIds() {

        List<String> ret = new ArrayList();
        if (spiderSnapshotSaveDao.existsById(SnapshotSaveConstant.FAVOR_ACCOUNT)) {
            WeixinSpiderSnapshotSaveEntity saveEntity = spiderSnapshotSaveDao.findByKey(SnapshotSaveConstant.FAVOR_ACCOUNT);
            String[] ids = StringUtils.split(saveEntity.getValue(), SnapshotSaveConstant.FAVOR_ACCOUNT_LIST_SEPARATOR);
            ret.addAll(ListUtils.arrayToList(ids));
        }
        return ret;
    }

    public List<WeixinPublicAccount> getAllFavorAccountsBySeq() {

        List<WeixinPublicAccount> accounts = new ArrayList<>();
        List<WeixinFavorAccountEntity>  accountEntities = favorAccountsDao.findAll();
        List<String> ids = getFavorSeqAccountFakeIds();
        for (String fid : ids) {
             Iterator<WeixinFavorAccountEntity> each = accountEntities.iterator();
            while (each.hasNext()) {
                WeixinFavorAccountEntity item = each.next();
                if (StringUtils.equals(fid,item.getFakeId())) {
                    each.remove();
                    accounts.add(entityToAccount(item));
                }
            }
        }
        // 剩下的账号直接放末尾就可以
        if (ListUtils.isNotEmpty(accountEntities)) {
            accountEntities.forEach(entity -> {
                accounts.add(entityToAccount(entity));
            });
        }
        //try {
        //
        //    System.out.println(new ObjectMapper().writeValueAsString(accounts));
        //} catch (Exception e) {
        //
        //}
        return accounts;
    }

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
