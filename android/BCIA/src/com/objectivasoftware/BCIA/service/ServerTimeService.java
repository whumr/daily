package com.objectivasoftware.BCIA.service;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.xml.sax.SAXException;

import android.os.Handler;
import android.os.Message;

import com.objectivasoftware.BCIA.model.ServerTime;
import com.objectivasoftware.BCIA.response.ServerTimeHandler;
import com.objectivasoftware.BCIA.util.ServiceCallUrl;
import com.objectivasoftware.BCIA.util.ServiceRequestUtil;
import com.objectivasoftware.BCIA.util.XmlUtil;

public class ServerTimeService {

	private Handler mHandler;
	public ServerTime serverTime;

	private static int TIME_UPDATA_SUCCESS = 11;
	private static int TIME_UPDATA_FAILD = 12;
	private final int CONNECT_ERROR = 3;
	private final int SERVER_ERROR = 4;

	/**
	 * @param mHandler
	 *            the mHandler to set
	 */
	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public void getData() {
		 getCurrentServerTimeAsync();
	}

	private void getCurrentServerTimeAsync() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					HttpResponse resp = ServiceRequestUtil.postRequest(
							ServiceCallUrl.GET_SERVER_TIME_URL, XmlUtil.getServerTimeXML());
					serverTime = getServerTime(resp.getEntity().getContent());
					if (serverTime.getException() != null) {
						Message msg = mHandler.obtainMessage(TIME_UPDATA_FAILD);
						mHandler.sendMessage(msg);
					} else {
						Message msg = mHandler.obtainMessage(TIME_UPDATA_SUCCESS);
						mHandler.sendMessage(msg);
					}
				} catch (SAXException e) {
					Message msg = mHandler.obtainMessage(SERVER_ERROR);
					mHandler.sendMessage(msg);
					e.printStackTrace();
				} catch (Exception e) {
					Message msg = mHandler.obtainMessage(CONNECT_ERROR);
					mHandler.sendMessage(msg);
					e.printStackTrace();
				}

			}
		});
		thread.start();
		// show loading
	}

	public ServerTime getServerTime(InputStream is)
			throws ParserConfigurationException, SAXException, IOException {
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ServerTimeHandler handler = new ServerTimeHandler();
		parser.parse(is, handler);
		is.close();
		return handler.getServerTime();
	}
}
