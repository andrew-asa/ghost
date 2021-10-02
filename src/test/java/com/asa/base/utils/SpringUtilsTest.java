package com.asa.base.utils;

import com.asa.utils.io.FilenameUtils;
import org.junit.Test;
import org.springframework.util.StringUtils;

/**
 * @author andrew_asa
 * @date 2021/9/29.
 */
public class SpringUtilsTest {

    @Test
    public void cleanPath() {
        System.out.println(StringUtils.cleanPath("a/b/c"));
        System.out.println(StringUtils.cleanPath("a/../b/"));
        System.out.println(StringUtils.cleanPath("../b/"));
        System.out.println(StringUtils.cleanPath("../../b/"));
        System.out.println(StringUtils.cleanPath("c/../../b/"));
    }
}
