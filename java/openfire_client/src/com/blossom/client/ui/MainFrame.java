package com.blossom.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.MUCInitialPresence;
import org.jivesoftware.smackx.packet.MUCOwner;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;

import com.blossom.client.entity.Friend;
import com.blossom.client.entity.FriendGroup;
import com.blossom.client.entity.Group;
import com.blossom.client.entity.Member;
import com.blossom.client.entity.Muc;
import com.blossom.client.entity.Node;
import com.blossom.client.entity.Title;
import com.blossom.client.packet.GroupApplyIQ;
import com.blossom.client.packet.GroupDestroyIQ;
import com.blossom.client.packet.GroupErrorIQ;
import com.blossom.client.packet.GroupMemberIQ;
import com.blossom.client.packet.GroupMessage;
import com.blossom.client.packet.GroupNewIQ;
import com.blossom.client.packet.GroupQueryIQ;
import com.blossom.client.packet.GroupQuitIQ;
import com.blossom.client.packet.extension.MucUserX;
import com.blossom.client.provider.GroupErrorIQProvider;
import com.blossom.client.provider.GroupMemberIQProvider;
import com.blossom.client.provider.GroupQueryIQProvider;
import com.blossom.client.provider.extension.FchtmlProvider;
import com.blossom.client.util.ChatManager;
import com.blossom.client.util.Constants;
import com.blossom.client.util.Session;

public class MainFrame extends BaseFrame {

	private static final long serialVersionUID = -540162592676462135L;
	
	private Session session;
	private ChatManager chatManager;
	
	private JTree listTree = new JTree();
	private JTree mucTree = new JTree();
	private JMenuBar menu = new JMenuBar();
	
	private DefaultMutableTreeNode group_node = new DefaultMutableTreeNode(new Title("群"));
	private DefaultMutableTreeNode root = new DefaultMutableTreeNode();;
	
	private DefaultMutableTreeNode muc_root = new DefaultMutableTreeNode(new Title("muc"));;
	
	public MainFrame() {
        session = Session.getSession();
        chatManager = ChatManager.getInstance();
		setTitle(session.getUsername());
		setSize(200, 500);
		setLayout(new BorderLayout());
		add(listTree, BorderLayout.CENTER);
		add(mucTree, BorderLayout.SOUTH);
		
		JMenu group_menu = new JMenu("群");
		JMenuItem group_new = new JMenuItem("新建");
		JMenuItem group_search = new JMenuItem("查找");
		JMenuItem group_delete = new JMenuItem("删除");
		JMenuItem group_exit = new JMenuItem("退出");
		JMenuItem group_rename = new JMenuItem("重命名");
		group_new.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputValue = JOptionPane.showInputDialog("输入群名称");
				if (inputValue == null) {
					JOptionPane.showMessageDialog(MainFrame.this, "群名称不能为空");
				} else if (!"".equals(inputValue.trim())) {
					createGroup(inputValue);
				}
			}
		});
		group_search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SearchFrame frame = new SearchFrame();
				ChatManager.getInstance().setPacketFrame(GroupQueryIQ.class, frame);
			}
		});
		group_delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Node node = (Node)((DefaultMutableTreeNode)listTree.getSelectionPath()
						.getLastPathComponent()).getUserObject();
				if (node instanceof Group) {
					Group group = (Group)node; 
					GroupDestroyIQ iq = new GroupDestroyIQ(group.getJid());
					session.addIq(iq);
					session.getConnection().sendPacket(iq);
				}
			}
		});
		group_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Node node = (Node)((DefaultMutableTreeNode)listTree.getSelectionPath()
						.getLastPathComponent()).getUserObject();
				if (node instanceof Group) {
					Group group = (Group)node; 
					GroupQuitIQ iq = new GroupQuitIQ(group.getJid());
					session.addIq(iq);
					session.getConnection().sendPacket(iq);
				}
			}
		});
		group_rename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Node node = (Node)((DefaultMutableTreeNode)listTree.getSelectionPath()
						.getLastPathComponent()).getUserObject();
				if (node instanceof Group) {
					String newName = JOptionPane.showInputDialog(MainFrame.this, "输入名称");
					Group group = (Group)node; 
					GroupMessage renameMessage = GroupMessage.createRenameMessage(newName, group.getJid());
					session.getConnection().sendPacket(renameMessage);
				}
			}
		});
		group_menu.add(group_new);
		group_menu.add(group_search);
		group_menu.add(group_delete);
		group_menu.add(group_exit);
		group_menu.add(group_rename);
		menu.add(group_menu);
		setJMenuBar(menu);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation( (screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        
        
        listTree.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		if (e.getClickCount() == 2) {
        			Node node = (Node)((DefaultMutableTreeNode)listTree.getSelectionPath()
    						.getLastPathComponent()).getUserObject();
        			if (node.getType() == Node.TYPE_FRIEND || node.getType() == Node.TYPE_GROUP) {
    					openChatDialog(node, null);
    				}
        		}
        	}
        });
        initListeners();
        loadFriends();
        loadGroups();
        
        loadMuc();
	}

	private void loadMuc() {
		XMPPConnection connection = session.getConnection();
		mucTree.setModel(new DefaultTreeModel(muc_root));
		DiscoverItems iq = new DiscoverItems();
		iq.setTo("c.mr.cn");
		connection.sendPacket(iq);
	}
	
	private void onLoadMuc(GroupQueryIQ groupQueryIQ) {
		muc_root.removeAllChildren();
		for (GroupQueryIQ.Item item : groupQueryIQ.getItems()) {
			muc_root.add(new DefaultMutableTreeNode(new Muc(item.getJid(), item.getName())));
		}
		mucTree.updateUI();
	}
	
	private void openChatDialog(Node node, Message msg) {
		String jid = node.getJid();
		ChatFrame frame = chatManager.getChat(jid);
		if (frame != null) {
			frame.toFront();
		} else {
			if (node instanceof Friend || node instanceof Member) 
				frame = new FriendChatFrame(node);
			else if (node instanceof Group)
				frame = new GroupChatFrame((Group)node);
			chatManager.addChat(node.getJid(), frame);
		}
		if (msg != null)
			frame.messageReceived(msg);
	}
	
	private void loadFriends() {
		XMPPConnection connection = session.getConnection();
		Roster roster = connection.getRoster();
		
		Collection<RosterGroup> groups = roster.getGroups();
		Collection<RosterEntry> unGroupedEntries = roster.getUnfiledEntries();
		
		for (RosterGroup group : groups) {
			DefaultMutableTreeNode group_node = new DefaultMutableTreeNode(new FriendGroup(group.getName()));
			for (RosterEntry group_roster : roster.getEntries()) {
				group_node.add(new DefaultMutableTreeNode(new Friend(group_roster.getUser(), group_roster.getName())));
			}
			root.add(group_node);
		}
		if (unGroupedEntries != null && !unGroupedEntries.isEmpty()) {
			DefaultMutableTreeNode unGroup = new DefaultMutableTreeNode(new FriendGroup("未分组好友"));
			root.add(unGroup);
			for (RosterEntry rosterEntry : unGroupedEntries) {
				unGroup.add(new DefaultMutableTreeNode(new Friend(rosterEntry.getUser(), rosterEntry.getName())));
			}
		}
		root.add(group_node);
		listTree.setModel(new DefaultTreeModel(root));
		listTree.setRootVisible(false);
		listTree.expandRow(0);
	}
	
	private void loadGroups() {
		GroupQueryIQ groupQueryIQ = new GroupQueryIQ();
		groupQueryIQ.setTo(Constants.SERVER_NAME);
		session.getConnection().sendPacket(groupQueryIQ);
	}
	
	protected void onLoadGroups(GroupQueryIQ groupQueryIQ) {
		group_node.removeAllChildren();
		for (GroupQueryIQ.Item item : groupQueryIQ.getItems()) {
			group_node.add(new DefaultMutableTreeNode(new Group(item.getJid(), item.getName())));
		}
		listTree.updateUI();
	}
	
	private void initListeners() {
		ProviderManager providerManager = ProviderManager.getInstance();
		providerManager.addIQProvider(GroupMemberIQ.ELEMENTNAME, GroupMemberIQ.NAMESPACE, new GroupMemberIQProvider());
		providerManager.addIQProvider(GroupQueryIQ.ELEMENTNAME, GroupQueryIQ.NAMESPACE, new GroupQueryIQProvider());
		providerManager.addIQProvider(GroupErrorIQ.ELEMENTNAME, GroupErrorIQ.NAMESPACE, new GroupErrorIQProvider());
		
//		providerManager.addExtensionProvider(GroupInformation.ELEMENTNAME, GroupInformation.NAMESPACE, new GroupInformationProvider());
		providerManager.addExtensionProvider(MucUserX.ELEMENTNAME, MucUserX.NAMESPACE, new MUCUserProvider());

		providerManager.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		providerManager.addExtensionProvider("fchtml", "jabber:client", new FchtmlProvider());
		
		ChatManager.getInstance().setPacketFrame(GroupQueryIQ.class, this);
		
		//群列表
		session.getConnection().addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				GroupQueryIQ groupQueryIQ = (GroupQueryIQ)packet;
				if (groupQueryIQ.getFrom().endsWith(Constants.SERVER_NAME)) {
					BaseFrame frame = ChatManager.getInstance().getPacketFrame(GroupQueryIQ.class);
					frame.onLoadGroups(groupQueryIQ);
				} else 
					onLoadMuc(groupQueryIQ);
				
			}
		}, new PacketFilter() {
			@Override
			public boolean accept(Packet packet) {
				return packet instanceof GroupQueryIQ;
			}
		});
		
		//成员列表
		session.getConnection().addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				GroupMemberIQ groupMemberIQ = (GroupMemberIQ)packet;
				String from = groupMemberIQ.getFrom();
				ChatFrame frame = chatManager.getChat(from);
				if (frame != null) {
					GroupChatFrame groupFrame = (GroupChatFrame)frame;
					groupFrame.memberIqReceived(groupMemberIQ);
				}
			}
		}, new PacketFilter() {
			@Override
			public boolean accept(Packet packet) {
				return packet instanceof GroupMemberIQ;
			}
		});
		
		//单聊消息
		session.getConnection().addPacketListener(new PacketListener() {
			
			@Override
			public void processPacket(Packet packet) {
				Message msg = (Message)packet;
				String from = msg.getFrom();
				
				//系统消息
				if (Constants.SERVER_NAME.equals(packet.getFrom())) {
					JOptionPane.showMessageDialog(MainFrame.this, ((Message)packet).getBody(), 
							"系统消息", JOptionPane.OK_OPTION);
				//重命名
				} else if (msg.getSubject() != null) {
					loadGroups();
				//邀请信息
				} else if (msg.getExtension("x", "http://jabber.org/protocol/muc#user") != null) {
					MUCUser x = (MUCUser)msg.getExtension("x", "http://jabber.org/protocol/muc#user");
					//回复邀请
					if (x.getInvite() != null) {
						MUCUser.Invite invite = x.getInvite();
						int i = JOptionPane.showConfirmDialog(MainFrame.this, invite.getReason(), 
								"入群邀请", JOptionPane.YES_NO_OPTION);
						String result = MucUserX.Decline.RESULT_DENIED;
						String reason = null;
						if (i == JOptionPane.YES_OPTION)
							result = MucUserX.Decline.RESULT_ACCEPT;
						else if (i == JOptionPane.NO_OPTION)
							reason = JOptionPane.showInputDialog(MainFrame.this, "输入拒绝理由");
						GroupMessage groupMessage = GroupMessage.createInviteReplyMessage(msg.getFrom(), 
								invite.getFrom(), result, reason);
						session.getConnection().sendPacket(groupMessage);
					//邀请反馈
					} else if (x.getDecline() != null) {
						String reason = x.getDecline().getReason();
						JOptionPane.showMessageDialog(MainFrame.this, reason);
					}
				} else {
					if (msg.getType() == Message.Type.groupchat)
						openChatDialog(new Group(msg.getFrom().split("/")[0], null), msg);
					else if (msg.getType() == Message.Type.chat)
						openChatDialog(new Friend(from.split("/")[0], from.split("@")[0]), msg);
				}
			}
		}, new PacketFilter() {
			@Override
			public boolean accept(Packet packet) {
				return packet instanceof Message;
			}
		});
		
		//反馈消息
		session.getConnection().addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				IQ stored = session.getIq(packet.getPacketID());
				IQ iq = (IQ)packet;
				if (iq instanceof GroupErrorIQ) {
					GroupErrorIQ error = (GroupErrorIQ)iq;
					JOptionPane.showMessageDialog(MainFrame.this, error.getMsg());
					System.out.println("failure request:" + stored.toXML());
					System.out.println("failure reply:" + packet.toXML());
				} else if (iq instanceof MUCOwner) {
					MUCOwner owner = (MUCOwner)iq;
					PacketExtension pe = owner.getExtension("x", "jabber:x:data");
					if (pe != null && pe instanceof DataForm) {
						MUCOwner reply = new MUCOwner();
						reply.setType(IQ.Type.SET);
						reply.setTo(iq.getFrom());
						reply.addExtension(new DataForm(Form.TYPE_SUBMIT));
						
						session.addIq(reply);
						session.getConnection().sendPacket(reply);
					}
					
				} else {
					System.out.println("success request:" + stored.toXML());
					System.out.println("success reply:" + packet.toXML());
					//刷新群列表
					if (stored instanceof GroupNewIQ || stored instanceof MUCOwner)
						loadGroups();
				} 
				session.removeIq(stored);
			}
		}, new PacketFilter() {
			@Override
			public boolean accept(Packet packet) {
				return session.containsIq(packet.getPacketID());
			}
		});
		
		//申请入群消息
		session.getConnection().addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				Registration reg = (Registration)packet;
//				<iq type='get' from='13@group.352.cn' to='user2@352.cn'>
//				<query xmlns='jabber:iq:register'>
//				  <applier>user1@352.cn</applier>
//				<reason><![CDATA[user1申请加入群：测试群，备注：我是xx]]></reason>
//				</query>
//				</iq>
				Map<String, String> map = reg.getAttributes();
				String group_id = reg.getFrom().split("@")[0];
				String applier = map.get("applier");
				String reason = map.get("reason");
				if (applier != null) {
					int i = JOptionPane.showConfirmDialog(MainFrame.this, 
							reason, "入群申请", JOptionPane.YES_NO_OPTION);
					String result = "";
					if (i == JOptionPane.YES_OPTION)
						result = GroupApplyIQ.RESULT_ACCEPT;
					else if (i == JOptionPane.NO_OPTION)
						result = GroupApplyIQ.RESULT_DENIED;
					GroupApplyIQ resultIQ = GroupApplyIQ.createResultIq(group_id, applier, result);
					session.addIq(resultIQ);
					session.getConnection().sendPacket(resultIQ);
				}
			}
		}, new PacketFilter() {
			@Override
			public boolean accept(Packet packet) {
				return packet instanceof Registration;
			}
		});
		
		//presence
		session.getConnection().addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				Presence pre = (Presence)packet;
				PacketExtension pe = pre.getExtension(MucUserX.ELEMENTNAME, MucUserX.NAMESPACE);
				if (pe != null) {
					String group_jid = pre.getFrom();
					GroupChatFrame groupFrame = chatManager.getChat(group_jid) == null ? null :
						(GroupChatFrame)chatManager.getChat(group_jid);
					MUCUser x = (MUCUser)pe;
					String code = x.getStatus() == null ? null : x.getStatus().getCode();
					String jid = x.getItem() == null ? null : x.getItem().getJid();
					//删除群、退出群、被踢出，刷新群列表
					if (x.getDestroy() != null || (session.getJID().equals(jid) && (
							MucUserX.Status.QUIT_CODE.equals(code) || MucUserX.Status.KICK_CODE.equals(code)))) {
						loadGroups();
						if (groupFrame != null) 
							groupFrame.dispose();
					//成员变化
					} else if (MucUserX.Status.KICK_CODE.equals(code) || MucUserX.Status.QUIT_CODE.equals(code)) {
						if (groupFrame != null) 
							groupFrame.loadMembers();
					//新成员加入
					} else if (MucUserX.Status.NEW_CODE.equals(code)) {
						MUCUser.Item item = x.getItem();
						if (item != null && item.getJid().equals(session.getJID()))
							loadGroups();
						else if (groupFrame != null) 
							groupFrame.loadMembers();
					//创建房间成功，开始配置
					} else if ("201".equals(code)) {
						MUCOwner mucowner = new MUCOwner();
						mucowner.setTo(packet.getFrom());
						
						session.addIq(mucowner);
						session.getConnection().sendPacket(mucowner);
					}
					
				} else {
//					JOptionPane.showMessageDialog(MainFrame.this, packet.toXML());
					System.out.println(packet.toXML());
				}
			}
		}, new PacketFilter() {
			@Override
			public boolean accept(Packet packet) {
				return packet instanceof Presence;
			}
		});
		
	}
	
	private void createGroup(String name) {
//		GroupNewIQ newIq = new GroupNewIQ(name);
//		System.out.println(newIq.toXML());
//		
//		session.addIq(newIq);
//		session.getConnection().sendPacket(newIq);
		
		
		Presence presence = new Presence(Presence.Type.available);
		presence.setTo(name + "@" + Constants.SERVER_NAME);
		presence.addExtension(new MUCInitialPresence());
		session.addPre(presence);
		session.getConnection().sendPacket(presence);
	}
}
