package com.ranbom.haque.util;

import org.pegdown.PegDownProcessor;

import java.io.*;

/**
 * @author dingj
 */

public class ParseMarkdown {

    public String generateHtml(File mdFile) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mdFile), "UTF-8"));
        String line = null;
        String mdContent = "";
        while ((line = br.readLine()) != null) {
            mdContent += line + "\r\n";
        }
        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
        String htmlContent = pdp.markdownToHtml(mdContent);

        return htmlContent;
    }


    public static void main(String[] args) throws IOException {
        ParseMarkdown pageGenerator = new ParseMarkdown();
        String res = pageGenerator.generateHtml(new File("F:\\Markdown\\树莓派使用内网穿透推送摄像头视频流.md"));
        System.out.println(res);
    }

}