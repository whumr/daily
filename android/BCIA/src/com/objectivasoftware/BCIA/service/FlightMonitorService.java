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

import com.objectivasoftware.BCIA.model.FlightMonitor;
import com.objectivasoftware.BCIA.model.SearchCondition;
import com.objectivasoftware.BCIA.response.FlightMonitorHandler;
import com.objectivasoftware.BCIA.util.ServiceCallUrl;
import com.objectivasoftware.BCIA.util.ServiceRequestUtil;
import com.objectivasoftware.BCIA.util.XmlUtil;

public class FlightMonitorService {
	public FlightMonitor flightMonitor;
	private SearchCondition searchCondition = new SearchCondition();

	
	public void setSearchCondition(SearchCondition searchCondition) {
		this.searchCondition = searchCondition;
	}

	private Handler mHandler;
	private static int SERVER_ERROR = 1;
	private final int CONNECT_ERROR = 2;
	private static int SEARCH_SUCCESS = 3;
	private static int SEARCH_FAILD = 4;
	
	 
	
	public void getView() {
		getViewAsync();
	}
	private void getViewAsync () {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
				HttpResponse resp = ServiceRequestUtil.postRequest(ServiceCallUrl.SEAECH_INFO_URL, XmlUtil.getSerchViewXml(searchCondition));

					flightMonitor = getFlightMonitor(resp.getEntity().getContent());
					if (flightMonitor.getException() != null) {
						Message msg = mHandler.obtainMessage(SEARCH_FAILD);
						mHandler.sendMessage(msg);
					} else {
						Message msg = mHandler.obtainMessage(SEARCH_SUCCESS);
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
		//show loading
	}
	public FlightMonitor getFlightMonitor (InputStream is) throws ParserConfigurationException, SAXException, IOException {
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		FlightMonitorHandler handler = new FlightMonitorHandler();
		parser.parse(is, handler);
		is.close();
		return handler.getFlightMonitor();
	}

	/**
	 * @param mHandler the mHandler to set
	 */
	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}
	
}
