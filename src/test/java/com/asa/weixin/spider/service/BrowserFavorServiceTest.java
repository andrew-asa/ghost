package com.asa.ghost.weixin.spider.service;

import junit.framework.TestCase;
import org.springframework.core.io.ClassPathResource;

/**
 * @author andrew_asa
 * @date 2021/4/9.
 */
public class BrowserFavorServiceTest extends TestCase {



    public void testImportFavor() throws Exception{
        BrowserFavorService service = new BrowserFavorService();
        ClassPathResource resource = new ClassPathResource("bookmarks_2021_4_9.html");
        service.parseChromeBrowserFavor(resource.getInputStream());
    }
}