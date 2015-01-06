package com.p2p.www.util;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.p2p.www.common.Common_Constants;
import com.p2p.www.model.Youku_user;

public class HttpUtil {

	private static String DEFAULT_CHARSET = "UTF-8";

	public static String getUserInfoStringByName(String user_name) throws IOException {
		return get(Common_Constants.URL.YOUKU_USER_INFO_URL_BY_NAME + user_name);
	}
	
	public static String getUserInfoStringById(String user_id) throws IOException {
		return get(Common_Constants.URL.YOUKU_USER_INFO_URL_BY_ID + user_id);
	}

	public static Youku_user getUserInfoById(String user_id) throws IOException {
		return getUserInfo(getUserInfoStringById(user_id));
	}

	public static Youku_user getUserInfoByName(String user_name) throws IOException {
		return getUserInfo(getUserInfoStringByName(user_name));
	}
	
	private static Youku_user getUserInfo(String result) {
		if (result != null) {
			JSONObject json = JSON.parseObject(result);
			if (!json.containsKey("error")) {
				return new Youku_user(json);
			} else {
				System.out.println(json.getString("error"));
			}
		}
		return null;
	}
	
	public static String getUserVedios(String user_name) throws IOException {
		return get(Common_Constants.URL.YOUKU_USER_VIDEOS_URL_BY_NAME + user_name);
	}
	
	public static String get(String url) throws IOException {
		return get(url, DEFAULT_CHARSET);
	}

	public static String get(String url, String charset) throws IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		HttpResponse response = httpClient.execute(get);
		return readResponse(response, charset);
	}
	
	private static String readResponse(HttpResponse response, String charset) throws IOException  {
		InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), charset);
		char[] chars = new char[4096];
		StringBuilder sb = new StringBuilder();
		int length = -1;
		while ((length = reader.read(chars)) > 0)
			sb.append(chars, 0, length);
		return sb.toString();
	}
}
