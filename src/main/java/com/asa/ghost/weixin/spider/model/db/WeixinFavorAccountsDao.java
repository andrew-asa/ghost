package com.asa.ghost.weixin.spider.model.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author andrew_asa
 * @date 2020/11/26.
 */
@Repository
public interface WeixinFavorAccountsDao extends JpaRepository<WeixinFavorAccountEntity, String>, JpaSpecificationExecutor<WeixinFavorAccountEntity>, Serializable {

    /**
     * 根据fakeid 查询公众号
     * @param fakeId
     * @return
     */
    Optional<WeixinFavorAccountEntity> findWeixinFavorAccountEntityByFakeId(String fakeId);

    Optional<WeixinFavorAccountEntity> findWeixinFavorAccountEntityByNickName(String nickName);

    Optional<WeixinFavorAccountEntity> findWeixinFavorAccountEntityByAlias(String alias);
}
