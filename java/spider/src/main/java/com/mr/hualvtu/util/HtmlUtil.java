package com.mr.hualvtu.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2015/1/8.
 */
public class HtmlUtil {

    private static HttpClient client = HttpClientBuilder.create().build();

    private static final int BUFFER_SIZE = 4096;

    public static String get(String url, String host, String charset) throws IOException {
        HttpGet get = new HttpGet(url);
        get.setHeader("Host", host);
        get.setHeader("Content-Type", "text/xml; charset=" + charset);
        HttpResponse response = client.execute(get);
        String reply = readResponse(response, charset);
        get.completed();
        return reply;
    }

    public static String post(String url, String params, String host, String charset) throws IOException {
        HttpPost post = new HttpPost(url);
        post.setHeader("Host", host);
        post.setHeader("Content-Type", "text/xml; charset=" + charset);
        post.setEntity(new StringEntity(params, charset));
        HttpResponse response = client.execute(post);
        String reply = readResponse(response, charset);
        post.completed();
        return reply;
    }

    private static String readResponse(HttpResponse response, String charset) throws IOException {
        InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), charset);
        char[] chars = new char[BUFFER_SIZE];
        StringBuilder sb = new StringBuilder();
        int length = -1;
        while ((length = reader.read(chars)) > 0)
            sb.append(chars, 0, length);
        return sb.toString();
    }
}
