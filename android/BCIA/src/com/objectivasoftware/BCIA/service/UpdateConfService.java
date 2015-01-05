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

import com.objectivasoftware.BCIA.model.Parks;
import com.objectivasoftware.BCIA.response.ParkHandler;
import com.objectivasoftware.BCIA.util.ServiceCallUrl;
import com.objectivasoftware.BCIA.util.ServiceRequestUtil;
import com.objectivasoftware.BCIA.util.XmlUtil;

public class UpdateConfService {
	private Handler mHandler;
	public Parks parks;
	
	private final int UPDATE_PARK_SUCCESS = 1;
	private final int UPDATE_PARK_FAILD = 2;
	private final int CONNECT_ERROR = 3;
	private final int SERVER_ERROR = 4;
	
	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}
	
	public void update() {
		updateConfAsync();
	}

	private void updateConfAsync() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					HttpResponse resp = ServiceRequestUtil.postRequest(ServiceCallUrl.PARK_INFO_URL, XmlUtil.getConfDataXML());
					parks = getResult(resp.getEntity().getContent());
					if (parks.getException() != null) {
						Message msg = mHandler.obtainMessage(UPDATE_PARK_FAILD);
						mHandler.sendMessage(msg);
					} else {
						Message msg = mHandler.obtainMessage(UPDATE_PARK_SUCCESS);
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
	}

	public Parks getResult(InputStream is)
			throws ParserConfigurationException, SAXException, IOException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ParkHandler handler = new ParkHandler();
		parser.parse(is, handler);
		is.close();
		return handler.getParks();
	}
}
