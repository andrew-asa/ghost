package com.asa.browser.interrupt;

import com.asa.utils.io.IOUtils;
import junit.framework.TestCase;
import org.springframework.core.io.ClassPathResource;

/**
 * @author andrew_asa
 * @date 2021/7/9.
 */
public class PluginProviderTest extends TestCase {

    public void testStringMaxLen() throws Exception{
        ClassPathResource resource = new ClassPathResource("com/asa/browser/third/jquery-3.6.0.js");
        String script = IOUtils.inputStream2String(resource.getInputStream());
        System.out.println(script);
    }
}