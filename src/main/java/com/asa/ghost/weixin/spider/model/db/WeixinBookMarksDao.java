package com.asa.ghost.weixin.spider.model.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * @author andrew_asa
 * @date 2020/11/26.
 * 书签
 */
@Repository
public interface WeixinBookMarksDao extends JpaRepository<WeixinBookMarksEntity, String>, JpaSpecificationExecutor<WeixinBookMarksEntity>, Serializable {

}
