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

import com.objectivasoftware.BCIA.model.FlightDeicingInput;
import com.objectivasoftware.BCIA.response.FlightDeicingInputHandler;
import com.objectivasoftware.BCIA.util.ServiceCallUrl;
import com.objectivasoftware.BCIA.util.ServiceRequestUtil;
import com.objectivasoftware.BCIA.util.XmlUtil;

public class FlightDeicingInputService {
	public FlightDeicingInput flightDeicingInput;
	private Handler mHandler;
	private String flightId;
	private final int CONNECT_ERROR = 3;
	private final int SERVER_ERROR = 4;
	private static final int GET_DATA_SUCCESS = 9;
	private static final int GET_DATA_FAILD = 10;
	private String searchNum = "";

	public void setSearchNum(String searchNum) {
		this.searchNum = searchNum;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public void getData() {
		 getInputTimeAsync();
	}

	private void getInputTimeAsync() {
		Thread thread = new Thread(new Runnable() {

			public void run() {
				try {
					HttpResponse resp = ServiceRequestUtil.postRequest(
							ServiceCallUrl.DEICING_INPUT_URL, XmlUtil.getInputDataXML(flightId, searchNum));

					flightDeicingInput = getFlightDeicingInput(resp.getEntity()
							.getContent());
					if (flightDeicingInput.getException() != null) {
						Message msg = mHandler.obtainMessage(GET_DATA_FAILD);
						mHandler.sendMessage(msg);
					} else {
						Message msg = mHandler.obtainMessage(GET_DATA_SUCCESS);
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

	public FlightDeicingInput getFlightDeicingInput(InputStream is)
			throws ParserConfigurationException, SAXException, IOException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		FlightDeicingInputHandler handler = new FlightDeicingInputHandler();
		
		parser.parse(is, handler);
		is.close();
		return handler.getFlightDeicingInput();
	}
}
