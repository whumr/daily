package com.blossom.client.util;

public class Constants {

	public static final String SERVER_NAME = "group.chat.352.cn";
	
	public static final String getGroupJid(String group_id) {
		return group_id + "@" + SERVER_NAME;
	}
}
