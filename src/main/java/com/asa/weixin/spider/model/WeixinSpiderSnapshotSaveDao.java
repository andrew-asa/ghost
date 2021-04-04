package com.asa.weixin.spider.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * @author andrew_asa
 * @date 2020/11/26.
 */
@Repository
public interface WeixinSpiderSnapshotSaveDao extends JpaRepository<WeixinSpiderSnapshotSaveEntity, String>, JpaSpecificationExecutor<WeixinSpiderSnapshotSaveEntity>, Serializable {

    /**
     * 根据key查询值
     * @param key
     * @return
     */
    WeixinSpiderSnapshotSaveEntity findByKey(String key);
}
