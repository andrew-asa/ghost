package com.asa.bilibili.lang;

import com.asa.exception.AbstractException;

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
