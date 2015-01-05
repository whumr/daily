package com.objectivasoftware.BCIA.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.xml.sax.SAXException;

import android.os.Handler;
import android.os.Message;

import com.objectivasoftware.BCIA.model.FlightDeicing;
import com.objectivasoftware.BCIA.response.FlightDeicingInfoHandler;
import com.objectivasoftware.BCIA.util.ServiceCallUrl;
import com.objectivasoftware.BCIA.util.ServiceRequestUtil;
import com.objectivasoftware.BCIA.util.XmlUtil;

public class FlightDeicingInfoService {

	public FlightDeicing flightdeicing;
	private Handler mHandler;
	private List<String> mList = new ArrayList<String>();
	
	public List<String> getmList() {
		return mList;
	}

	public void setmList(List<String> mList) {
		this.mList = mList;
	}

	private static int GET_VIEW_SUCCESS = 5;
	private static int GET_VIEW_FAILD = 6;
	private static int LIST_NONE = 7;
	private final int CONNECT_ERROR = 3;
	private final int SERVER_ERROR = 4;
	
	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}
	
	private void getViewAsync () {
		//send post / parse xml
		Thread thread = new Thread(new Runnable() {
			HttpResponse resp;
			public void run() {
				try {

					if (getmList().size() == 0) {
						Message msg = mHandler.obtainMessage(LIST_NONE);
						mHandler.sendMessage(msg);
					} else {
						resp = ServiceRequestUtil.postRequest(ServiceCallUrl.DEICING_INFO_URL, XmlUtil.getDeicingInfoXML(getmList()));

						flightdeicing = getFlightDeicing(resp.getEntity().getContent());
						if (flightdeicing.getException() != null) {
							Message msg = mHandler.obtainMessage(GET_VIEW_FAILD);
							mHandler.sendMessage(msg);
						} else {
							Message msg = mHandler.obtainMessage(GET_VIEW_SUCCESS);
							mHandler.sendMessage(msg);
						}

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
	public FlightDeicing getFlightDeicing (InputStream is) throws ParserConfigurationException, SAXException, IOException {
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		FlightDeicingInfoHandler handler = new FlightDeicingInfoHandler();
		parser.parse(is, handler);
		is.close();
		return handler.getFlightdeicing();
	}

	public void getView() {
		getViewAsync();
	}
	
}
