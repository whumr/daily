package com.blossom.client.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class Main {

	static ConnectionConfiguration config;
	static Connection connection;
	static {
		config = new ConnectionConfiguration("127.0.0.1", 5222);
		config.setCompressionEnabled(true);
		config.setSASLAuthenticationEnabled(true);
		connection = new XMPPConnection(config);
		try {
			connection.connect();
			connection.login("mr1", "mr");
			
			connection.addPacketListener(new PacketListener() {
				
				@Override
				public void processPacket(Packet packet) {
					System.out.println(packet.toXML());
					System.out.println("------received--------");
				}
			}, new PacketFilter() {
				
				@Override
				public boolean accept(Packet packet) {
					return packet != null;
				}
			});
			
			connection.addPacketInterceptor(new PacketInterceptor() {
				
				@Override
				public void interceptPacket(Packet packet) {
					System.out.println(packet.toXML());
					System.out.println("------send--------");
				}
			}, new PacketFilter() {
				
				@Override
				public boolean accept(Packet packet) {
					return packet != null;
				}
			});
			
		} catch (XMPPException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		createRoom();
	}
	
	private static void createRoom() {
		try {  
			// 使用XMPPConnection创建一个MultiUserChat
			MultiUserChat muc = new MultiUserChat(connection, "room2@c.mr.cn");
			// 创建聊天室
			muc.create("i_im_admin");
            // 获得聊天室的配置表单  
            Form form = muc.getConfigurationForm();  
            // 根据原始表单创建一个要提交的新表单。  
            Form submitForm = form.createAnswerForm();  
            
            
            // 向要提交的表单添加默认答复  
            for (Iterator<FormField> fields = form.getFields(); fields.hasNext();) {  
                FormField field = fields.next();  
                if (!FormField.TYPE_HIDDEN.equals(field.getType())  
                        && field.getVariable() != null) {  
                    // 设置默认值作为答复  
                    submitForm.setDefaultAnswer(field.getVariable());  
                }  
            }  
            // 设置聊天室的新拥有者  
            // List owners = new ArrayList();  
            // owners.add("liaonaibo2\\40slook.cc");  
            // owners.add("liaonaibo1\\40slook.cc");  
//            submitForm.setAnswer("muc#roomconfig_roomowners", "mr\\40pc-20130902qyhw/Spark 2.6.3");  
            // 设置聊天室是持久聊天室，即将要被保存下来  
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);  
            // 房间仅对成员开放  
            submitForm.setAnswer("muc#roomconfig_membersonly", false);  
            // 允许占有者邀请其他人  
            submitForm.setAnswer("muc#roomconfig_allowinvites", true);  
            // 能够发现占有者真实 JID 的角色  
            List<String> list = new ArrayList<String>();
            list.add("anyone");
            submitForm.setAnswer("muc#roomconfig_whois", list);  
            // 登录房间对话  
            submitForm.setAnswer("muc#roomconfig_enablelogging", true);  
            // 仅允许注册的昵称登录  
            submitForm.setAnswer("x-muc#roomconfig_reservednick", true);  
            // 允许使用者修改昵称  
            submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);  
            // 允许用户注册房间  
            submitForm.setAnswer("x-muc#roomconfig_registration", true);  
            // 发送已完成的表单（有默认值）到服务器来配置聊天室  
            muc.sendConfigurationForm(submitForm);  
        } catch (XMPPException e) {  
            e.printStackTrace();  
        }  
		disconnect();
	}
	
	private static void disconnect() {
		if(connection.isConnected())
			connection.disconnect();
		connection = null;
	}
}
