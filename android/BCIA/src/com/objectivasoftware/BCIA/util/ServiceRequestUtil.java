package com.objectivasoftware.BCIA.util;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

public class ServiceRequestUtil {
	private static int SET_CONNECTION_TIMEOUT = 60000;
	private static int SET_SOCKET_TIMEOUT = 30000;
 	private static HttpResponse response;
	public static HttpResponse postRequest(String url, String xml) throws Exception {
		
		BasicHttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, SET_CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, SET_SOCKET_TIMEOUT);
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost();
		request.setHeader("Content-type", "application/x-www-form-urlencoded");
		try {
			request.setURI(new URI(url));
			StringEntity entity = new StringEntity(xml, "UTF-8");
			entity.setChunked(true);
			request.setEntity(entity);
			response = client.execute(request);
			return response;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
}
