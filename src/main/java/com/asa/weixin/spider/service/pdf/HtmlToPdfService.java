package com.asa.weixin.spider.service.pdf;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author andrew_asa
 * @date 2021/3/31.
 */
@Component
public class HtmlToPdfService {

    public  int convert(String htmlUrl, String filePath) throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append("wkhtmltopdf ");
        sb.append(htmlUrl).append(" ").append(filePath);
        String command =  sb.toString();
        Process process = Runtime.getRuntime().exec(command);
        return process.waitFor();
    }
}
