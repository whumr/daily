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

import com.objectivasoftware.BCIA.model.CheckBoxState;
import com.objectivasoftware.BCIA.model.DeicingStnd;
import com.objectivasoftware.BCIA.response.ParksPitemsHandler;
import com.objectivasoftware.BCIA.util.ServiceCallUrl;
import com.objectivasoftware.BCIA.util.ServiceRequestUtil;
import com.objectivasoftware.BCIA.util.XmlUtil;

public class ParkService {
	private Handler mHandler;
	private CheckBoxState checkBoxState;
	public DeicingStnd deicingStnd;
	
	private static int GET_STND_SUCCESS = 15;
	private static int GET_STND_FAILD = 16;
	private final int CONNECT_ERROR = 3;
	private final int SERVER_ERROR = 4;

	/**
	 * @param mHandler
	 *            the mHandler to set
	 */
	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public void setChange() {
		 setChangeAsync();
	}

	private void setChangeAsync() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					HttpResponse resp = ServiceRequestUtil.postRequest(ServiceCallUrl.GET_PARK_STNDS_URL, XmlUtil.getParkstateXml(checkBoxState));

					deicingStnd = getParkStnd(resp.getEntity().getContent());
					if (deicingStnd.getException() != null) {
						Message msg = mHandler.obtainMessage(GET_STND_FAILD);
						mHandler.sendMessage(msg);
					} else {
						Message msg = mHandler.obtainMessage(GET_STND_SUCCESS);
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

	public DeicingStnd getParkStnd(InputStream is) throws ParserConfigurationException,
			SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ParksPitemsHandler handler = new ParksPitemsHandler();
		parser.parse(is, handler);
		is.close();
		return handler.getDeicingStnd();
	}

	/**
	 * @param checkBoxState
	 *            the checkBoxState to set
	 */
	public void setCheckBoxState(CheckBoxState checkBoxState) {
		this.checkBoxState = checkBoxState;
	}
}
