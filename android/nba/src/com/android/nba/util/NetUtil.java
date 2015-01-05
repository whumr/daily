package com.android.nba.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

import com.android.nba.domain.Constants.System_Config;
import com.android.nba.domain.Constants.System_Static;

public class NetUtil {

	private static final String LOG = "NetUtil";
	
	/**
	 * 读取指定日期的比赛信息
	 * @param date
	 * @return
	 * @throws IOException
	 */
	public static String getMatchInfo(String date) throws IOException {
		return getURLContent(System_Static.MATCHURL.replaceFirst("#", date));
	}
	
	/**
	 * 获取比分信息
	 * @param mid
	 * @param date
	 * @return
	 * @throws IOException
	 */
	public static String getScores(int mid, String date) throws IOException {
		return getURLContent(System_Static.SCOREURL.replaceFirst("#1", mid + "").replaceFirst("#2", date));
	}
	
	/**
	 * 获取比分信息
	 * @param mid
	 * @param date
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String getScores(int mid, String date, String encoding) throws IOException {
		return getURLContent(System_Static.SCOREURL.replaceFirst("#1", mid + "").replaceFirst("#2", date), encoding);
	}
	
	/**
	 * 读取指定链接的内容
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String getURLContent(String url) throws IOException {
		return getURLContent(url, System_Config.DEFAULT_ENCODING);
	}
	
	/**
	 * 读取指定链接的内容
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String getURLContent(String url, String encoding) throws IOException {
		try {
			URL myURL = new URL(url);
			URLConnection ucon = myURL.openConnection();
			InputStream is = ucon.getInputStream();
			String content = null;
			byte[] buffer = new byte[System_Config.BYTEBUFFER_SIZE];
			ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(System_Config.BYTEBUFFER_SIZE);
			int readCount = 0;
			while ((readCount = is.read(buffer)) != -1) {
				byteArrayBuffer.append(buffer, 0, readCount);
			}
			content = new String(byteArrayBuffer.toByteArray(), encoding);
			Log.d(LOG, "get page " + url);
			return content;
		//找不到页面
		} catch(FileNotFoundException e) {
			Log.e(LOG, url + " page not found.");
			return null;
		}
	}
	
	
}
