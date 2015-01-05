package com.blossom.client.test;

import java.util.Collection;
import java.util.Iterator;
import javax.net.SocketFactory;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Session;
import org.jivesoftware.smack.packet.Message.Type;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
 
/**
 * <b>function:</b> ����Smack������ XMPP Э��ͨ��
 * @author hoojo
 * @createDate 2012-5-22 ����10:28:18
 * @file ConnectionServerTest.java
 * @package com.hoo.smack.conn
 * @project jwchat
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class SmackXMPPTest {
 
    private Connection connection;
    private ConnectionConfiguration config;
    /** openfire������address */
    private final static String server = "127.0.0.1";
    
    private final void fail(Object o) {
        if (o != null) {
            System.out.println(o);
        }
    }
    
    private final void fail(Object o, Object... args) {
        if (o != null && args != null && args.length > 0) {
            String s = o.toString();
            for (int i = 0; i < args.length; i++) {
                String item = args[i] == null ? "" : args[i].toString();
                if (s.contains("{" + i + "}")) {
                    s = s.replace("{" + i + "}", item);
                } else {
                    s += " " + item;
                }
            }
            System.out.println(s);
        }
    }
    
    /**
     * <b>function:</b> ��ʼSmack��openfire���������ӵĻ�������
     * @author hoojo
     * @createDate 2012-6-25 ����04:06:42
     */
    @Before
    public void init() {
        try {
            //connection = new XMPPConnection(server);
            //connection.connect();
            /** 5222��openfire������Ĭ�ϵ�ͨ�Ŷ˿ڣ�����Ե�¼http://192.168.8.32:9090/������Ա����̨�鿴�ͻ��˵��������˿� */
            config = new ConnectionConfiguration(server, 5222);
            
            /** �Ƿ�����ѹ�� */ 
            config.setCompressionEnabled(true);
            /** �Ƿ����ð�ȫ��֤ */
            config.setSASLAuthenticationEnabled(true);
            /** �Ƿ����õ��� */
            config.setDebuggerEnabled(false);
            //config.setReconnectionAllowed(true);
            //config.setRosterLoadedAtLogin(true);
            
            /** ����connection���� */
            connection = new XMPPConnection(config);
            /** �������� */
            connection.connect();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        fail(connection);
        fail(connection.getConnectionID());
    }
    
    @After
    public void destory() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }
    }
    
    /**
     * <b>function:</b> ConnectionConfiguration �Ļ������������Ϣ
     * @author hoojo
     * @createDate 2012-6-25 ����04:11:25
     */
    @Test
    public void testConfig() {
        fail("PKCS11Library: " + config.getPKCS11Library());
        fail("ServiceName: {0}", config.getServiceName());
        // ssl֤������
        fail("TruststorePassword: {0}", config.getTruststorePassword());
        fail("TruststorePath: {0}", config.getTruststorePath());
        fail("TruststoreType: {0}", config.getTruststoreType());
        
        SocketFactory socketFactory = config.getSocketFactory();
        fail("SocketFactory: {0}", socketFactory);
        /*try {
            fail("createSocket: {0}", socketFactory.createSocket("localhost", 3333));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    
    /**
     * <b>function:</b> Connection ����������Ϣ
     * @author hoojo
     * @createDate 2012-6-25 ����04:12:04
     */
    @Test
    public void testConnection() {
        /** �û����� */
        AccountManager accountManager = connection.getAccountManager();
        for (String attr : accountManager.getAccountAttributes()) {
            fail("AccountAttribute: {0}", attr);
        }
        fail("AccountInstructions: {0}", accountManager.getAccountInstructions());
        /** �Ƿ����� */
        fail("isConnected:", connection.isConnected());
        fail("isAnonymous:", connection.isAnonymous());
        /** �Ƿ���Ȩ�� */
        fail("isAuthenticated:", connection.isAuthenticated());
        fail("isSecureConnection:", connection.isSecureConnection());
        /** �Ƿ�ʹ��ѹ�� */
        fail("isUsingCompression:", connection.isUsingCompression());
    }
    
    /**
     * <b>function:</b> �û�������
     * @author hoojo
     * @createDate 2012-6-25 ����04:22:31
     */
    @Test
    public void testAccountManager() {
        AccountManager accountManager = connection.getAccountManager();
        for (String attr : accountManager.getAccountAttributes()) {
            fail("AccountAttribute: {0}", attr);
        }
        fail("AccountInstructions: {0}", accountManager.getAccountInstructions());
        
        fail("supportsAccountCreation: {0}", accountManager.supportsAccountCreation());
        try {
            /** ����һ���û�boy������Ϊboy��������ڹ���Ա����̨ҳ��http://192.168.8.32:9090/user-summary.jsp�鿴�û�/��������Ϣ�����鿴�Ƿ�ɹ������û� */
            accountManager.createAccount("boy", "boy");
            /** �޸����� */
            accountManager.changePassword("abc");
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUser() {
        try {
            /** �û���½���û��������� */
            connection.login("hoojo", "hoojo");
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        /** ��ȡ��ǰ��½�û� */
        fail("User:", connection.getUser());
        
        /** �����û��� */
        Roster roster = connection.getRoster();
        
        /** �����û��飬�������Spark����û����ѣ���������Ϳ��Բ�ѯ����ص����� */
        Collection<RosterEntry> rosterEntiry = roster.getEntries();
        Iterator<RosterEntry> iter = rosterEntiry.iterator();
        while (iter.hasNext()) {
            RosterEntry entry = iter.next();
            fail("Groups: {0}, Name: {1}, Status: {2}, Type: {3}, User: {4}", entry.getGroups(), entry.getName(), entry.getStatus(), entry.getType(), entry);
        }
        
        fail("-------------------------------");
        /** δ������֤���ѣ���ӹ��ĺ��ѣ�û�еõ��Է�ͬ�� */
        Collection<RosterEntry> unfiledEntries = roster.getUnfiledEntries();
        iter = unfiledEntries.iterator();
        while (iter.hasNext()) {
            RosterEntry entry = iter.next();
            fail("Groups: {0}, Name: {1}, Status: {2}, Type: {3}, User: {4}", entry.getGroups(), entry.getName(), entry.getStatus(), entry.getType(), entry);
        }
    }
    
    @Test
//    @SuppressWarnings("static-access")
    public void testPacket() {
        try {
            connection.login("hoojo", "hoojo");
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        
        //Packet packet = new Data(new DataPacketExtension("jojo@" + server, 2, "this is a message"));
        //connection.sendPacket(packet);
        
        /** �����û�״̬��available=true��ʾ���ߣ�false��ʾ���ߣ�status״̬ǩ���������½����Spark�ͻ�������оͿ��Կ������½��״̬ */
        Presence presence = new Presence(Presence.Type.available);
        presence.setStatus("Q�Ұ�");
        connection.sendPacket(presence);
        
        Session session = new Session();
//        String sessid = session.nextID();
        connection.sendPacket(session);
        /** ��jojo@192.168.8.32 ����������Ϣ����ʱ����Ҫ��Spark�����½jojo����û���
         * ��������Ϳ�����jojo����û�����������Ϣ��Spark��½��jojo�û��Ϳ��Խ��յ���Ϣ
         **/
        /** Type.chat ��ʾ���죬groupchat�������죬error����headline�����û��� */
        Message message = new Message("jojo@" + server, Type.chat);
        //Message message = new Message(sessid, Type.chat);
        message.setBody("h!~ jojo, I'am is hoojo!");
        connection.sendPacket(message);
        
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * <b>function:</b> ����������Ϣ������
     * @author hoojo
     * @createDate 2012-6-25 ����05:03:23
     */
    @Test
    public void testChatManager() {
        /** ����״̬ */
        try {
            connection.login("hoojo", "hoojo");
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        
        /** ����״̬ */
        Presence presence = new Presence(Presence.Type.available);
        presence.setStatus("Q�Ұ�");
        connection.sendPacket(presence);
        
        /** ��ȡ��ǰ��½�û������������ */
        ChatManager chatManager = connection.getChatManager();
        /** Ϊָ���û�����һ��chat��MyMessageListeners���ڼ����Է�����������Ϣ  */
        Chat chat = chatManager.createChat("jojo@" + server, new MyMessageListeners());
        try {
            /** ������Ϣ */
            chat.sendMessage("h!~ jojo����");
            
            /** ��message��������Ϣ */
            Message message = new Message();
            message.setBody("message");
            message.setProperty("color", "red");
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * <b>function:</b> ��Ϣ���������û������Է����͵���Ϣ��Ҳ������Է�������Ϣ
     * @author hoojo
     * @createDate 2012-6-25 ����05:05:31
     * @file SmackXMPPTest.java
     * @package com.hoo.smack
     * @project jwchat
     * @blog http://blog.csdn.net/IBM_hoojo
     * @email hoojo_@126.com
     * @version 1.0
     */
    class MyMessageListeners implements MessageListener {
        public void processMessage(Chat chat, Message message) {
            try {
                /** ������Ϣ */
                chat.sendMessage("dingding����" + message.getBody());
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            /** ������Ϣ */
            fail("From: {0}, To: {1}, Type: {2}, Sub: {3}", message.getFrom(), message.getTo(), message.getType(), message.toXML());
            /*Collection<Body> bodys =  message.getBodies();
            for (Body body : bodys) {
                fail("bodies[{0}]", body.getMessage());
            }
            //fail(message.getLanguage());
            //fail(message.getThread());
            //fail(message.getXmlns());*/
            fail("body: ", message.getBody());
        }
    }
}