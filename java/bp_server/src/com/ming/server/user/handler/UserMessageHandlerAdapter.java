package com.ming.server.user.handler;

import com.ming.server.common.domain.Protocol;
import com.ming.server.handler.MessageAdapter;
import com.ming.server.user.business.UserService;

public class UserMessageHandlerAdapter extends MessageAdapter implements UserMessageHandler {

	protected UserService userService;
	
	@Override
	public void handleMessage(Protocol protocol) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createUser(Protocol protocol) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loginUser(Protocol protocol) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteUser(Protocol protocol) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyUser(Protocol protocol) {
		// TODO Auto-generated method stub

	}

	@Override
	public void searchUser(Protocol protocol) {
		// TODO Auto-generated method stub

	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
