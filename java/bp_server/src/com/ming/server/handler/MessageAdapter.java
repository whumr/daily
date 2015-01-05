package com.ming.server.handler;

import org.apache.log4j.Logger;

import com.ming.server.common.domain.Constants;
import com.ming.server.common.domain.Protocol;
import com.ming.server.common.domain.Request;
import com.ming.server.common.domain.Response;
import com.ming.server.util.ProtocolFactory;

public class MessageAdapter implements MessageHandler {

	private static Logger logger = Logger.getLogger(MessageAdapter.class);
	
	@Override
	public void handleMessage(Protocol protocol) {
		// TODO Auto-generated method stub

	}
	
	protected Response createDefaultResponse(Request request) {
		try {
			Response response = (Response)ProtocolFactory.getProtocol(Integer.toBinaryString(request.getVersion()), 
					Constants.RESPONSE_CODE);
			response.setVersion(request.getVersion());
			response.setSocket(request.getSocket());
			response.setOpCode(request.getOpCode());
			response.setResponseCode(request.getSubCode());
			response.setContent(Constants.DEFAULT_RESPONSE);
			response.setContentLength(Constants.DEFAULT_RESPONSE.length());
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("error create response", e);
			return null;
		}
	}
}