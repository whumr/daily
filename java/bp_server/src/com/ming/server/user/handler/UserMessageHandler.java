package com.ming.server.user.handler;

import com.ming.server.common.domain.Protocol;
import com.ming.server.handler.MessageHandler;

public interface UserMessageHandler extends MessageHandler {

	public void createUser(Protocol protocol);
	public void loginUser(Protocol protocol);
	public void deleteUser(Protocol protocol);
	public void modifyUser(Protocol protocol);
	public void searchUser(Protocol protocol);
}
