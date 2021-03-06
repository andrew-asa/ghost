package com.asa.ghost.bilibili.lang;


import com.asa.base.utils.StringUtils;

/**
 * @author andrew_asa
 * @date 2021/10/2.
 */
public class NoDataResponseException extends BilibiliAbstractException {

    public static final String ERROR_CODE = StringUtils.EMPTY;

    public NoDataResponseException(String msg) {

        super(ERROR_CODE, msg);
    }
}
