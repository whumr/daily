package com.blossom.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.MUCAdmin;

import com.blossom.client.entity.Group;
import com.blossom.client.entity.Member;
import com.blossom.client.packet.GroupMemberIQ;
import com.blossom.client.packet.GroupMessage;
import com.blossom.client.packet.extension.Fchtml;
import com.blossom.client.util.ChatManager;
import com.blossom.client.util.Session;

public class GroupChatFrame extends ChatFrame {

	private static final long serialVersionUID = 5554840131491154558L;

	private Group group;
	private Session session;
	private XMPPConnection connection;
	
	DefaultListModel listModel = new DefaultListModel();
	private JList hisList = new JList(listModel);
	private JPanel hisPanel = new JPanel();
	private JTree memberTree = new JTree();
	private JPanel memberPanel = new JPanel();
	private JTextArea msgText = new JTextArea(5, 10);
	private JPanel msgPanel = new JPanel();
	private JMenuBar menu = new JMenuBar();
	JMenu member_menu = new JMenu("成员");
	JMenuItem member_kick = new JMenuItem("踢出");
	JMenuItem member_invite = new JMenuItem("邀请");
	
	public GroupChatFrame(Group group) {
		this.group = group;
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setTitle(group.getJid());
		setLayout(new BorderLayout());
		setSize(400, 400);
		hisPanel.setSize(500, 300);
		hisPanel.add(hisList);
		hisPanel.setBorder(new TitledBorder("聊天记录"));
		
		msgPanel.setSize(500, 100);
		msgPanel.add(msgText);
		
		memberPanel.setSize(100, 400);
		memberPanel.add(memberTree);
		memberPanel.setBorder(new TitledBorder("成员列表"));
		
		add(hisPanel, BorderLayout.CENTER);
		add(memberPanel, BorderLayout.EAST);
		add(msgText, BorderLayout.SOUTH);
		
		member_menu.add(member_kick);
		member_menu.add(member_invite);
		menu.add(member_menu);
		setJMenuBar(menu);
		
		setVisible(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation( (screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

        init();
	}
	
	private void init() {
		session = Session.getSession();
        connection = session.getConnection();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ChatManager.getInstance().removeChat(group.getJid());
				dispose();
			}
		});
		
		msgText.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
					final String msg = msgText.getText();
					
					Message message = new Message(group.getJid(), Message.Type.groupchat);
//					message.setBody(msgText.getText());
					message.addExtension(new Fchtml(msg));
					connection.sendPacket(message);
					msgText.setText("");
				}
			}
		});
		
		memberTree.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		//私聊
        		if (e.getClickCount() == 2) {
        			Member member = (Member)((DefaultMutableTreeNode)memberTree.getSelectionPath()
    						.getLastPathComponent()).getUserObject();
        			String jid = member.getJid();
        			if (!jid.equals(session.getJID())) {
        				ChatManager chatManager = ChatManager.getInstance();
        				ChatFrame frame = chatManager.getChat(jid);
        				if (frame != null) {
        					frame.toFront();
        				} else {
        					frame = new FriendChatFrame(member);
        					chatManager.addChat(jid, frame);
        				}
        			}
        		}
        	}
        });
		
		member_kick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (memberTree.getSelectionPath() != null) {
					Member member = (Member)((DefaultMutableTreeNode)memberTree.getSelectionPath()
							.getLastPathComponent()).getUserObject();
					String jid = member.getJid();
					if (!jid.equals(session.getJID())) {
						String input = JOptionPane.showInputDialog(GroupChatFrame.this, "输入清退理由");
						if (input != null) {
//							GroupMemberIQ kickIq = GroupMemberIQ.createKickMemberIq(jid, group.getJid(), input);
							
							MUCAdmin mucAdmin = new MUCAdmin();
							MUCAdmin.Item item = new MUCAdmin.Item("member", "none");
							item.setJid(jid);
							item.setReason(input);
							mucAdmin.addItem(item);
							mucAdmin.setTo(group.getJid());
							mucAdmin.setType(IQ.Type.SET);
							
							connection.sendPacket(mucAdmin);
							session.addIq(mucAdmin);
						}
					}
				}
			}
		});
		
		member_invite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog(GroupChatFrame.this, "输入邀请用户");
				if (input != null) {
					String jid = input;
					String reason = JOptionPane.showInputDialog(GroupChatFrame.this, "输入邀请理由");
					GroupMessage inviteMessage = GroupMessage.createInviteMessage(group.getJid(), jid, reason);
					connection.sendPacket(inviteMessage);
				}
			}
		});
		loadMembers();
	}

	@Override
	public void messageReceived(Message msg) {
		String content = msg.getBody();
		if (content == null || "".equals(content))
			content = ((Fchtml)msg.getExtension("fchtml", "jabber:client")).getMsg();
		listModel.addElement(msg.getFrom().split("/")[1] + ":" + content);
	}
	
	public void memberIqReceived(GroupMemberIQ groupMemberIQ) {
		List<GroupMemberIQ.Item> list = groupMemberIQ.getItems();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		for (GroupMemberIQ.Item member : list) {
			DefaultMutableTreeNode member_node = new DefaultMutableTreeNode(new Member(member.getJid(), 
					member.getNick(), group.getJid().split("@")[0]));
			root.add(member_node);
		}
		memberTree.setModel(new DefaultTreeModel(root));
		memberTree.setRootVisible(false);
//		memberTree.expandRow(0);
	}

	public void loadMembers() {
		connection.sendPacket(GroupMemberIQ.createMemberQueryIq(session.getJID(), group.getJid()));
	}
}
