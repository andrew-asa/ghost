package com.asa.bilibili.lang;

import com.asa.exception.AbstractException;
import com.asa.utils.StringUtils;

/**
 * @author andrew_asa
 * @date 2021/10/2.
 */
public class ResponseCodeException extends AbstractException {

    public ResponseCodeException(String msg) {
        super(StringUtils.EMPTY, msg);
    }
}
