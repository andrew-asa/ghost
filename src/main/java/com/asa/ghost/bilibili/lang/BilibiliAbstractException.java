package com.asa.ghost.bilibili.lang;


import com.asa.base.exception.AbstractException;

/**
 * @author andrew_asa
 * @date 2021/10/3.
 */
public class BilibiliAbstractException extends AbstractException {

    public BilibiliAbstractException() {

    }

    public BilibiliAbstractException(String errorCode, String message) {

        super(errorCode, message);
    }
}
