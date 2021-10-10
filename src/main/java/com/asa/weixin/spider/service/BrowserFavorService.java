package com.asa.weixin.spider.service;

import com.asa.base.utils.StringUtils;
import com.asa.weixin.spider.model.FavorItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;

/**
 * @author andrew_asa
 * @date 2021/4/9.
 * 浏览器收藏夹服务器
 */
public class BrowserFavorService {

    public static final String DEFAULT_CHARSET_NAME = "utf-8";
    public static final String DEFAULT_BASE_URI = "https://asa.com";

    /**
     * 解析浏览器书签栏
     * @param in
     * @throws Exception
     */
    public FavorItem parseChromeBrowserFavor(InputStream in) throws Exception{

        return parseChromeBrowserFavor(in, DEFAULT_CHARSET_NAME, DEFAULT_BASE_URI);
    }

    /**
     * 导入收藏夹
     */
    public FavorItem parseChromeBrowserFavor(InputStream in, String charsetName, String baseUri) throws Exception{

        Document doc = Jsoup.parse(in, charsetName, baseUri);
        Element bodyE = doc.selectFirst("body");
        if (bodyE != null) {
            FavorItem root = new FavorItem();
            root.setIsParent();
            String rootText = bodyE.select("h1").text();
            Element rootElement = bodyE.selectFirst("dl");
            Elements children = rootElement.children();
            root.setName(rootText);
            children.forEach(child->{
                FavorItem favorItem =  parseElement(child);
                if (favorItem != null) {
                    root.appendChild(favorItem);
                }
            });
            return root;
        }
        return null;
    }

    private FavorItem parseElement(Element element) {

        if (isElement(element, "p")) {
            return null;
        }
        // dt 标签才操作
        if (isElement(element , "dt")) {
            FavorItem root = new FavorItem();
            Elements children = element.children();
            if (children != null) {
                Element[] chidrensE =  children.toArray(new Element[0]);
                for (int i = 0; i < chidrensE.length; i++) {
                    Element child = chidrensE[i];
                    // 叶子节点
                    if (isElement(child, "a")) {
                        root.setName(child.text());
                        root.setUrl(child.attr("href"));
                        root.setIsChildren();
                        return root;
                    }
                    root.setIsParent();
                    // 根节点
                    if (isElement(child, "h3")) {
                        root.setName(child.text());
                    }
                    // 含有子元素
                    if (isElement(child, "dl")) {
                        Elements childrenE = child.children();
                        childrenE.forEach(item->{
                            FavorItem favorItem =  parseElement(item);
                            if (favorItem != null) {
                                root.appendChild(favorItem);
                            }
                        });
                    }
                }
            }
            return root;
        }
        return null;
    }

    private boolean isElement(Element element, String tag) {

        return  element!=null && StringUtils.equalsIgnoreCase(element.tag().getName(), tag);
    }
}
