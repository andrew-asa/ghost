package com.asa.base.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author andrew_asa
 * @date 2021/10/3.
 */
public class ObjectMapperUtils {

    public static ObjectMapper default_mapper = new ObjectMapper();

    public static ObjectMapper getDefaultMapper() {

        return default_mapper;
    }
}
