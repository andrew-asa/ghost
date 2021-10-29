package com.asa.ghost.bilibili.lang;

import com.asa.base.utils.StringUtils;

/**
 * @author andrew_asa
 * @date 2021/10/3.
 * 需要登录才能进行操作异常
 */
public class RequireLoginException extends BilibiliAbstractException {

    public static final String ERROR_CODE = StringUtils.EMPTY;

    public RequireLoginException(String msg) {

        super(ERROR_CODE, msg);
    }
}
