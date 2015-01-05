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
import com.objectivasoftware.BCIA.model.Result;
import com.objectivasoftware.BCIA.response.UpdateHandler;
import com.objectivasoftware.BCIA.util.ServiceCallUrl;
import com.objectivasoftware.BCIA.util.ServiceRequestUtil;
import com.objectivasoftware.BCIA.util.XmlUtil;

public class UpdateService {
	public Result result;
	private String flightId;
	
	private static int UPDATA_SUCCESS = 18;
	private static int UPDATA_FAILD = 17;
	private final int CONNECT_ERROR = 3;
	private final int SERVER_ERROR = 4;
	
	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public void setDeicingNum(String deicingNum) {
		this.deicingNum = deicingNum;
	}

	private String deicingNum;
	
	private Handler mHandler;
	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}
	private FlightDeicingInput flightDeicingInput;

	public void setFlightDeicingInput(FlightDeicingInput flightDeicingInput) {
		this.flightDeicingInput = flightDeicingInput;
	}

	public void updata() {
		updataAsync();
	}

	private void updataAsync() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					HttpResponse resp = ServiceRequestUtil.postRequest(ServiceCallUrl.UPDATE_INFO_URL, XmlUtil.inputDataXML(flightDeicingInput, flightId, deicingNum));
					result = getResult(resp.getEntity().getContent());
					if (result.getException() != null) {
						Message msg = mHandler.obtainMessage(UPDATA_FAILD);
						mHandler.sendMessage(msg);
					} else {
						Message msg = mHandler.obtainMessage(UPDATA_SUCCESS);
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

	public Result getResult(InputStream is)
			throws ParserConfigurationException, SAXException, IOException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		UpdateHandler handler = new UpdateHandler();
		parser.parse(is, handler);
		is.close();
		return handler.getResult();
	}
}
