package com.blossom.client.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class UserChat {
	public static void chatuser(final Connection conn) {

		ChatManager manager = conn.getChatManager();
		final MessageListener messagelistener = new MessageListener() {

			@Override
			public void processMessage(Chat chat, Message message) {
				System.out.println(conn.getUser() + " Received message:" + message.getBody());
			}

		};
		
		manager.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				if (!createdLocally)
					chat.addMessageListener(messagelistener);
			}
		});

	}
	
	//������������������ӡ��û���¼��������Ϣ���Ǵ�����ʵ�ֵ�
	public static void main(String[] args) {
		ConnectionConfiguration config = new ConnectionConfiguration("127.0.0.1", 5222); 
		// ��һ�����������openfire��������ַ��5222��openfireͨ�ŵĶ˿�
		config.setCompressionEnabled(true);
		config.setSASLAuthenticationEnabled(true);

		Connection connection = new XMPPConnection(config);
		Connection conn = new XMPPConnection(config);

		try {
			connection.connect();
			conn.connect();
			connection.login("mr1", "mr");
			conn.login("mr2", "mr");

			System.out.println(conn.getConnectionID());
			
//			mr@pc-20130902qyhw/Spark 2.6.3
			
			ChatManager chatManager = connection.getChatManager();
			Chat chat = chatManager.createChat("mr2@pc-20130902qyhw/Smack",
					new MessageListener() { 
				// �����һ�������ǽ����˵ĵ�ַ������ֻд�û�����������ʲô��������������System.out.println(conn.getUser());���û��ľ����ʾ������ӡ��������
						@Override
						public void processMessage(Chat chat, Message message) {
							System.out.println("Received message:" + message);
						}
					});
			
			while (true) {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String message = "";
				System.out.println(connection.getUser() + " Please input the message:");
				try {
					message = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				chatuser(conn);
				chat.sendMessage(message);
			}
		} catch (XMPPException e) {
			e.printStackTrace();
			System.out.println("Error Delivering block");
		}

		connection.disconnect();
	}
}
