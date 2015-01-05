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
			// ʹ��XMPPConnection����һ��MultiUserChat
			MultiUserChat muc = new MultiUserChat(connection, "room2@c.mr.cn");
			// ����������
			muc.create("i_im_admin");
            // ��������ҵ����ñ�  
            Form form = muc.getConfigurationForm();  
            // ����ԭʼ������һ��Ҫ�ύ���±���  
            Form submitForm = form.createAnswerForm();  
            
            
            // ��Ҫ�ύ�ı����Ĭ�ϴ�  
            for (Iterator<FormField> fields = form.getFields(); fields.hasNext();) {  
                FormField field = fields.next();  
                if (!FormField.TYPE_HIDDEN.equals(field.getType())  
                        && field.getVariable() != null) {  
                    // ����Ĭ��ֵ��Ϊ��  
                    submitForm.setDefaultAnswer(field.getVariable());  
                }  
            }  
            // ���������ҵ���ӵ����  
            // List owners = new ArrayList();  
            // owners.add("liaonaibo2\\40slook.cc");  
            // owners.add("liaonaibo1\\40slook.cc");  
//            submitForm.setAnswer("muc#roomconfig_roomowners", "mr\\40pc-20130902qyhw/Spark 2.6.3");  
            // �����������ǳ־������ң�����Ҫ����������  
            submitForm.setAnswer("muc#roomconfig_persistentroom", true);  
            // ������Գ�Ա����  
            submitForm.setAnswer("muc#roomconfig_membersonly", false);  
            // ����ռ��������������  
            submitForm.setAnswer("muc#roomconfig_allowinvites", true);  
            // �ܹ�����ռ������ʵ JID �Ľ�ɫ  
            List<String> list = new ArrayList<String>();
            list.add("anyone");
            submitForm.setAnswer("muc#roomconfig_whois", list);  
            // ��¼����Ի�  
            submitForm.setAnswer("muc#roomconfig_enablelogging", true);  
            // ������ע����ǳƵ�¼  
            submitForm.setAnswer("x-muc#roomconfig_reservednick", true);  
            // ����ʹ�����޸��ǳ�  
            submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);  
            // �����û�ע�᷿��  
            submitForm.setAnswer("x-muc#roomconfig_registration", true);  
            // ��������ɵı�����Ĭ��ֵ����������������������  
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
