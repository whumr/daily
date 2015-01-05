package com.ming.server.user.handler.v101;

import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.ming.server.common.domain.Protocol;
import com.ming.server.common.domain.Request;
import com.ming.server.common.domain.Response;
import com.ming.server.domain.exception.DaoException;
import com.ming.server.domain.ext.UserEx;
import com.ming.server.user.handler.UserMessageHandlerAdapter;
import com.ming.server.util.ProtocolFactory;

public class UserMessageHandlerImpl extends UserMessageHandlerAdapter {

	private static Logger logger = Logger.getLogger(UserMessageHandlerImpl.class);
	
	@Override
	public void createUser(Protocol protocol) {
		// TODO Auto-generated method stub
		if(protocol instanceof Request) {
			Request request = (Request)protocol;
			try {
				userService.createUser(UserEx.getUserByJson(request.getContent()));
				Response response = createDefaultResponse(request);
				ProtocolFactory.getProtocolUtil(Integer.toBinaryString(response.getVersion()))
					.writeProtocol(new DataOutputStream(response.getSocket().getOutputStream()), response, true);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("createUser error", e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("createUser error", e);
			}
		}
	}

}