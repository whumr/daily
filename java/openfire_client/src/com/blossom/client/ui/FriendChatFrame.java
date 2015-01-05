package com.blossom.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import com.blossom.client.entity.Member;
import com.blossom.client.entity.Node;
import com.blossom.client.packet.GroupMessage;
import com.blossom.client.util.ChatManager;
import com.blossom.client.util.Constants;
import com.blossom.client.util.Session;

public class FriendChatFrame extends ChatFrame {

	private static final long serialVersionUID = -913301438675605679L;

	private Node friend;
	private XMPPConnection connection;
	
	DefaultListModel listModel = new DefaultListModel();
	private JList hisList = new JList(listModel);
	private JPanel hisPanel = new JPanel();
	private JTextArea msgText = new JTextArea(5, 10);
	private JPanel msgPanel = new JPanel();
	
	public FriendChatFrame(Node friend) {
		this.friend = friend;
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle(friend.getJid());
		if (friend instanceof Member)
			setTitle(friend.getJid() + "(来自群[" + ((Member)friend).getGroup_id() + "])");
		setLayout(new BorderLayout());
		setSize(500, 400);
		hisPanel.setSize(500, 300);
		hisPanel.add(hisList);
		hisPanel.setBorder(new TitledBorder("聊天记录"));
		
		msgPanel.setSize(500, 100);
		msgPanel.add(msgText);
		
		add(hisPanel, BorderLayout.CENTER);
		add(msgText, BorderLayout.SOUTH);
		
		init();
		
		setVisible(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation( (screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}
	
	private void init() {
		connection = Session.getSession().getConnection();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ChatManager.getInstance().removeChat(friend.getJid());
				dispose();
			}
		});
		
		msgText.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Message message = new Message(friend.getJid(), Message.Type.chat);
					message.setBody(msgText.getText());
					if (friend instanceof Member) {
						Member member = (Member)friend;
						message = GroupMessage.createPrivateMessage(Constants.getGroupJid(member.getGroup_id()), 
								member.getJid(), msgText.getText());
					}
					connection.sendPacket(message);
					msgText.setText("");
					msgText.setCaretPosition(0);
					message.setFrom(Session.getSession().getJID());
					messageReceived(message);
				}
			}
		});
	}

	@Override
	public void messageReceived(Message msg) {
		listModel.addElement(msg.getFrom() + ":" + msg.getBody());
	}

}
