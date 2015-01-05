package com.blossom.test.util;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.util.JiveGlobals;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

import com.blossom.test.component.Constants;
import com.blossom.test.component.Constants.ATTRIBUTE_TAG;
import com.blossom.test.component.Constants.ATTRIBUTE_VALUE;
import com.blossom.test.component.Constants.NAMESPACE;
import com.blossom.test.component.Constants.TAG;
import com.blossom.test.dao.PageList;
import com.blossom.test.entity.Group;
import com.blossom.test.entity.GroupApply;
import com.blossom.test.entity.GroupMember;
import com.blossom.test.entity.GroupMessage;
import com.blossom.test.util.GroupError.Condition;

public class PacketUtil {
	
	private String serverDomain;
	
	private static PacketUtil packetUtil;
	private ThreadPoolExecutor sendPacketPool;
	
	private PacketUtil() {
		serverDomain = JiveGlobals.getProperty(Constants.COMPONENT_NAME_KEY, Constants.DEFAULT_COMPONENT_NAME)
				+ '.' + XMPPServer.getInstance().getServerInfo().getXMPPDomain();
		
		int poolSize = JiveGlobals.getIntProperty("xmpp.httpbind.worker.threads", 
				// use deprecated property as default (shared with ConnectionManagerImpl)
				JiveGlobals.getIntProperty("xmpp.client.processing.threads", 16));
        int keepAlive = JiveGlobals.getIntProperty("xmpp.httpbind.worker.timeout", 60);
        
		sendPacketPool = new ThreadPoolExecutor (poolSize, poolSize, keepAlive, TimeUnit.SECONDS, 
				new LinkedBlockingQueue<Runnable>(), // unbounded task queue
		        new ThreadFactory() { // custom thread factory for BOSH workers
		            final AtomicInteger counter = new AtomicInteger(1);
		            public Thread newThread(Runnable runnable) {
		                Thread thread = new Thread(Thread.currentThread().getThreadGroup(), runnable,
		                                    "groupComponent-worker-" + counter.getAndIncrement());
		                thread.setDaemon(true);
		                return thread;
		            }
		    	});
	}
	
	public static PacketUtil getInstance() {
		if (packetUtil == null)
			packetUtil = new PacketUtil();
		return packetUtil;
	}
	
	/**
	 * 生成入群申请需要转发给群主的iq
	 * 
	 * @param group
	 * @param applier
	 * @param apply_id
	 * @param reason
	 * @return
	 */
	public IQ createGroupApplyMessage(Group group, String applier, String reason) {
//		<iq type='get' from='13@group.352.cn' to='user2@352.cn'>
//		<query xmlns='jabber:iq:register'>
//		  <applier>user1@352.cn</applier>
//		<reason><![CDATA[user1申请加入群：测试群，备注：我是xx]]></reason>
//		</query>
//		</iq>
		IQ iq = new IQ(IQ.Type.get);
		iq.setFrom(groupAsFrom(group.getId()));
		iq.setTo(group.getCreator());
		Element query = iq.setChildElement(TAG.QUERY, NAMESPACE.JABBER_REGISTER);
		Element applier_e = query.addElement(TAG.APPLIER);
		applier_e.setText(applier);
		Element reason_e = query.addElement(TAG.REASON);
		StringBuilder buffer = new StringBuilder();
		buffer.append("用户[").append(getJidNode(applier)).append("]申请加入群[")
			.append(group.getGroup_name()).append("].")
			.append("申请理由：").append(reason);
		reason_e.addCDATA(buffer.toString());
		return iq;
	}
	
	/**
	 * 生成审批结果的通知消息，发过申请人
	 * 
	 * @param applier
	 * @param apply_id
	 * @param result
	 * @param group_name
	 * @return
	 */
	public Message createGroupApplyResponseMessage(GroupApply apply, Group group) {
//		<message from='group.352.cn' to='user1@352.cn' type='groupchat'>
//		    <body><![CDATA[群主user2拒绝/通过了你的入群申请.]]</body>
//		</message>
		String result = GroupApply.STATUS_ACCEPT.equals(apply.getStatus()) ? "通过" : "拒绝";
		StringBuilder buffer = new StringBuilder();
		buffer.append("群主[").append(group.getCreator()).append("]")
			.append(result).append("了你加入[").append(group.getGroup_name())
			.append("]的申请");
		return createSysMessage(apply.getApply_user(), buffer.toString());
	}
	
	/**
	 * 生成踢出成员的通知消息，发給被踢出的成员
	 * 
	 * @param member_jid
	 * @param group
	 * @param reason
	 * @return
	 */
	public Message createKickMemberResponseMessage(String member_jid, Group group, String reason) {
//		<message from='group.352.cn' to='user2@352.cn' type='groupchat'>
//		    <body><![CDATA[你被请出了群[测试群].]]</body>
//		</message>
		StringBuilder buffer = new StringBuilder();
		buffer.append("你被请出了群[").append(group.getGroup_name())
			.append("].");
		if (reason != null)
			buffer.append("原因:").append(reason);
		return createSysMessage(member_jid, buffer.toString());
	}
	
	/**
	 * 生成系统消息
	 * 
	 * @param to
	 * @param content
	 * @return
	 */
	private Message createSysMessage(String to, String content) {
		Element msg_e = DocumentFactory.getInstance().createDocument().addElement("message");
		msg_e.addElement(TAG.BODY).addCDATA(content);
		Message msg = new Message(msg_e);
		msg.setType(Message.Type.groupchat);
		msg.setFrom(serverDomain);
		msg.setTo(to);
		return msg;
	}
	
	/**
	 * 生成成员被踢出的消息，发送给群成员
	 * 
	 * @param member_jid
	 * @param group
	 * @return
	 */
	public Presence createMemberOutPresence(String member_jid, Group group) {
//		<presence from='13@group.352.cn' to='usern@352.cn' type='unavailable'>
//		  <x xmlns='http://jabber.org/protocol/muc#user'>
//			<item affiliation='none' jid='user2@352.cn' nick='user2' role='none'/>
//			<status code='307'/>
//		  </x>
//		</presence>
		Presence pre = new Presence(Presence.Type.unavailable);
		pre.setFrom(serverDomain);
		Element x = pre.addChildElement(TAG.X, NAMESPACE.MUC_USER);
		Element item = x.addElement(TAG.ITEM);
		item.addAttribute(ATTRIBUTE_TAG.AFFILIATION, ATTRIBUTE_VALUE.NONE);
		item.addAttribute(ATTRIBUTE_TAG.JID, member_jid);
		item.addAttribute(ATTRIBUTE_TAG.NICK, getJidNode(member_jid));
		item.addAttribute(ATTRIBUTE_TAG.ROLE, ATTRIBUTE_VALUE.NONE);
		Element status = x.addElement(TAG.STATUS);
		status.addAttribute(ATTRIBUTE_TAG.CODE, Constants.CODE_KICKMEMBER);
		return pre;
	}
	
	/**
	 * 生成新成员加入的通知信息，发送给群成员
	 * 
	 * @return
	 */
	public Presence createNewMemberPresence(GroupApply apply) {
//		<presence from='13@group.352.cn' to='usern@352.cn'>
//		  <x xmlns='http://jabber.org/protocol/muc#user'>
//		<item affiliation='member' jid='user1@352.cn' nick='user1' role='member'/>
//		<status code='301'/>
//		  </x>
//		</presence>
		Presence pre = new Presence();
		pre.setFrom(groupAsFrom(apply.getGroup_id()));
		Element x = pre.addChildElement(TAG.X, NAMESPACE.MUC_USER);
		Element item = x.addElement(TAG.ITEM);
		item.addAttribute(ATTRIBUTE_TAG.AFFILIATION, ATTRIBUTE_VALUE.MEMBER);
		item.addAttribute(ATTRIBUTE_TAG.JID, apply.getApply_user());
		item.addAttribute(ATTRIBUTE_TAG.NICK, getJidNode(apply.getApply_user()));
		item.addAttribute(ATTRIBUTE_TAG.ROLE, GroupMember.ROLE_DEFAULT);
		Element status = x.addElement(TAG.STATUS);
		status.addAttribute(ATTRIBUTE_TAG.CODE, Constants.CODE_NEWMEMBER);
		return pre;
	}

	/**
	 * 新建群，发送给创建者，设置群属性
	 * 
	 * @return
	 */
	public Presence createGroupCreatePresence(Group group, String to) {
//		<presence to="mr2@mr.cn/Spark 2.6.3" from="aaa@c.mr.cn/mr2">
//			<x xmlns="http://jabber.org/protocol/muc#user">
//				<item jid="mr2@mr.cn/Spark 2.6.3" affiliation="owner" role="moderator"/>
//				<status code="201"/>
//			</x>
//		</presence>
		Presence pre = new Presence();
		pre.setFrom(groupAsFrom(group.getId()));
		pre.setTo(to);
		Element x = pre.addChildElement(TAG.X, NAMESPACE.MUC_USER);
		Element item = x.addElement(TAG.ITEM);
		item.addAttribute(ATTRIBUTE_TAG.AFFILIATION, ATTRIBUTE_VALUE.OWNER);
		item.addAttribute(ATTRIBUTE_TAG.JID, group.getCreator());
		item.addAttribute(ATTRIBUTE_TAG.ROLE, "moderator");
		Element status = x.addElement(TAG.STATUS);
		status.addAttribute(ATTRIBUTE_TAG.CODE, Constants.CODE_NEWGROUP);
		return pre;
	}
	
	/**
	 * 发送成员上线的通知
	 * 
	 * @param online_member
	 * @param others
	 */
	public void sendMemberOnOfflinePresence(GroupMember online_member, List<GroupMember> others, boolean online) {
		for (GroupMember member : others) {
			sendPacket(createMemberOnOfflinePresence(online_member, member.getMember_jid(), online), 
					ComponentManagerFactory.getComponentManager());
		}
	}
	
	/**
	 * 生成成员上线的通知
	 * 
	 * @param member
	 * @return
	 */
	public Presence createMemberOnOfflinePresence(GroupMember member, String to, boolean online) {
//		<presence from='13@group.352.cn' to='user1@352.cn' [type='unavailable']>
//		  <x xmlns='http://jabber.org/protocol/muc#user'>
//		    <item affiliation='member' jid='user2@352.cn' nick='user2' role='admin'/>
//		  </x>
//		</presence>
		Presence pre = new Presence();
		if (!online)
			pre.setType(Presence.Type.unavailable);
		pre.setFrom(groupAsFrom(member.getGroup_id()));
		pre.setTo(to);
		Element x = pre.addChildElement(TAG.X, NAMESPACE.MUC_USER);
		Element item = x.addElement(TAG.ITEM);
		item.addAttribute(ATTRIBUTE_TAG.AFFILIATION, ATTRIBUTE_VALUE.MEMBER);
		item.addAttribute(ATTRIBUTE_TAG.JID, member.getMember_jid());
		item.addAttribute(ATTRIBUTE_TAG.NICK, member.getMember_nick());
		item.addAttribute(ATTRIBUTE_TAG.ROLE, member.getRole());
		return pre;
	}

	/**
	 * 生成群成员的结果列表
	 * 
	 * @param request
	 * @param members
	 * @return
	 */
	public IQ createMemberQueryResponse(IQ request, List<GroupMember> members) {
		IQ iq = IQ.createResultIQ(request);
		Element query = iq.setChildElement(TAG.QUERY, NAMESPACE.MUC_ADMIN);
		if (members != null && !members.isEmpty()) {
			for (GroupMember groupMember : members) {
				Element member = query.addElement(TAG.ITEM);
				member.addAttribute(ATTRIBUTE_TAG.AFFILIATION, ATTRIBUTE_VALUE.MEMBER);
				member.addAttribute(ATTRIBUTE_TAG.JID, groupMember.getMember_jid());
				member.addAttribute(ATTRIBUTE_TAG.NICK, groupMember.getMember_nick());
				member.addAttribute(ATTRIBUTE_TAG.ROLE, groupMember.getRole());
				member.addAttribute(ATTRIBUTE_TAG.ONLINE, String.valueOf(groupMember.isOnline()));
			}
		}
		return iq;
	}
	
	public IQ createErrorIq(IQ iq, Condition condition) {
		IQ error = IQ.createResultIQ(iq);
		setError(error, condition);
        return error;
	}
	
	private void setError(IQ iq, Condition condition) {
		Element error = DocumentFactory.getInstance().createElement(TAG.ERROR, NAMESPACE.ERROR);
		error.addAttribute(ATTRIBUTE_TAG.CODE, String.valueOf(condition.getCode()));
		error.addAttribute(ATTRIBUTE_TAG.TYPE, condition.getValue());
		error.addText(condition.getMsg());
		iq.getElement().add(error);
	}
	
	public IQ createErrorIqText(String from, String to, String msg) {
		IQ error = new IQ(IQ.Type.result);
        if (from != null && !"".equals(from.trim()))
        	error.setFrom(from.trim());
        else
        	error.setFrom(serverDomain);
        error.setTo(to);
        error.getElement().setText(msg);
        return error;
	}
	
	public Presence createQuitPresence(String member_jid, long group_id) {
//		<presence from='13@group.352.cn' to='usern@352.cn' type='unavailable'>
//		<x xmlns='http://jabber.org/protocol/muc#user'>
//		  <item affiliation='none' jid='user1@352.cn' nick='none' role='none'/>
//		<status code='308'/>
//		</x>
//		</presence>
		Presence pre = new Presence(Presence.Type.unavailable);
		pre.setFrom(groupAsFrom(group_id));
		Element x = pre.addChildElement(TAG.X, NAMESPACE.MUC_USER);
		Element member = x.addElement(TAG.ITEM);
		member.addAttribute(ATTRIBUTE_TAG.AFFILIATION, ATTRIBUTE_VALUE.NONE);
		member.addAttribute(ATTRIBUTE_TAG.JID, member_jid);
		member.addAttribute(ATTRIBUTE_TAG.NICK, ATTRIBUTE_VALUE.NONE);
		member.addAttribute(ATTRIBUTE_TAG.ROLE, ATTRIBUTE_VALUE.NONE);
		Element status = x.addElement(TAG.STATUS);
		status.addAttribute(ATTRIBUTE_TAG.CODE, Constants.CODE_QUITEMBER);
		return pre;
	}
	
	/**
	 * 生成一个删除群之后的成员通知信息
	 * 
	 * @param group_id
	 * @param reason
	 * @return
	 */
	public Presence createGroupDeletePresence(long group_id, String reason) {
//		<presence from='group.352.cn' to='usern@352.cn' type='unavailable'>
//		  <x xmlns='http://jabber.org/protocol/muc#user'>
//		    <destroy jid='13@group.352.cn'>
//		      <reason><![CDATA[不想要了]]></reason>
//		    </destroy>
//		  </x>
//		</presence>
		Presence pre = new Presence(Presence.Type.unavailable);
		pre.setFrom(serverDomain);
		Element x = pre.addChildElement(TAG.X, NAMESPACE.MUC_USER);
		Element destroy = x.addElement(TAG.DESTROY);
		destroy.addAttribute(ATTRIBUTE_TAG.JID, groupAsFrom(group_id));
		if (reason != null && !"".equals(reason.trim())) {
			Element reason_e = destroy.addElement(TAG.REASON);
			reason_e.addCDATA(reason);
		}
		return pre;
	}
	
	/**
	 * 生成查询群列表的返回结果
	 * 
	 * @param request
	 * @param list
	 * @param isSearch
	 * @return
	 */
	public IQ createGroupSearchResult(IQ request, List<Group> list, boolean isSearch) {
//		<iq from='group.352.cn' to='user1@352.cn' type='result'>
//	    <query xmlns='http://jabber.org/protocol/disco#items'>
//	         <item jid='13@group.352.cn' name='测试群'/>
//	         <item jid='15@group.352.cn' name='测试群1'/>
//	         <item jid='17@group.352.cn' name='测试群2'/>
//	         <set xmlns='http://jabber.org/protocol/rsm' page='1' pagesize='10' count='8' />
//	    </query>
//	</iq>
		IQ reply = IQ.createResultIQ(request);
		reply.setFrom(serverDomain);
		Element query = reply.setChildElement(TAG.QUERY, NAMESPACE.DISCO_ITEMS);
        //items
		if (list != null && !list.isEmpty()) {
        	for (Group group : list) {
        		Element item = query.addElement(TAG.ITEM);
        		item.addAttribute(ATTRIBUTE_TAG.JID, groupAsFrom(group.getId()));
        		item.addAttribute(ATTRIBUTE_TAG.NAME, group.getGroup_name());
        	}
        	//查询，返回总条数
        	if (isSearch) {
        		PageList<Group> pageList = (PageList<Group>)list;
        		Element set = query.addElement(TAG.SET, NAMESPACE.JABBER_RSM);
        		set.addAttribute(ATTRIBUTE_TAG.PAGE, String.valueOf(pageList.getPage()));
        		set.addAttribute(ATTRIBUTE_TAG.PAGESIZE, String.valueOf(pageList.getPage_size()));
        		set.addAttribute(ATTRIBUTE_TAG.COUNT, String.valueOf(pageList.getCount()));
        	}
        }
        return reply;
	}
	
	/**
	 * 生成群历史消息
	 * 
	 * @param msg
	 * @param receiver
	 * @return
	 */
	public Message createHisMessage(GroupMessage groupMessage, String receiver) {
//		<message from='user2@group.352.cn' to='user1@352.cn' type='groupchat'>
//		  <body><![CDATA[hello world.]]</body>
//		  <delay xmlns='urn:xmpp:delay' stamp='2013-12-09 01:00:00'/>
//		</message>
		Element msg_e = DocumentFactory.getInstance().createDocument().addElement("message");
		msg_e.addElement(TAG.BODY).addCDATA(groupMessage.getContent());
		Message msg = new Message(msg_e);
		msg.setType(Message.Type.groupchat);
		msg.setTo(receiver);
		msg.setFrom(groupMessage.getSender());
		Element delay = msg.addChildElement(TAG.DELAY, NAMESPACE.URN_XMPP_DELAY);
		delay.addAttribute(ATTRIBUTE_TAG.STAMP, Constants.DEFAULT_SDF.format(groupMessage.getCreate_time()));
		return msg;
	}
	
	/**
	 * 生成更新群名称的消息
	 * 
	 * @param group_id
	 * @param new_name
	 * @param from
	 * @return
	 */
	public Message createRenameMessage(long group_id, String new_name, String from) {
//		<message from='13@group.352.cn' to='usern@352.cn' type='groupchat'>
//		  <subject>新群名称</subject>
//		</message>
		Element msg_e = DocumentFactory.getInstance().createDocument().addElement("message");
		msg_e.addElement(TAG.SUBJECT).addCDATA(new_name);
		Message msg = new Message(msg_e);
		msg.setType(Message.Type.groupchat);
		msg.setFrom(from);
		return msg;
	}
	
	/**
	 * 生成邀请消息，发送给被邀请人
	 * 
	 * @param group
	 * @param from
	 * @param invitee
	 * @param reason
	 * @return
	 */
	public Message createInviteMessage(Group group, String from, String invitee, String reason) {
//		<message from='13@group.352.cn' to='user2@352.cn'>
//			<x xmlns='http://jabber.org/protocol/muc#user'>
//				<invite from='user1@352.cn'>
//					<reason><![CDATA[我是xx]]></reason>
//				</invite>
//			</x>
//		</message>
		Message msg = new Message();
		msg.setType(Message.Type.groupchat);
		msg.setTo(invitee);
		msg.setFrom(groupAsFrom(group.getId()));
		Element x = msg.addChildElement(TAG.X, NAMESPACE.MUC_USER);
		Element invite = x.addElement(TAG.INVITE);
		invite.addAttribute(ATTRIBUTE_TAG.FROM, from);
		Element reason_e = invite.addElement(TAG.REASON);
		if (reason == null || "".equals(reason.trim()))
			reason = "";
		reason_e.addCDATA(getJidNode(from) + "邀请您加入群[" + group.getGroup_name() + "].备注：" + reason);
		return msg;
	}
	
	/**
	 * 生成被邀请人的应答消息，发送给邀请人
	 * 
	 * @param group
	 * @param inviter
	 * @param invitee
	 * @param reason
	 * @param result
	 * @return
	 */
	public Message createInviteResultMessage(Group group, String inviter, String invitee, String reason, String result) {
//		<message from='13@group.352.cn' to='user1@352.cn'>
//			<x xmlns='http://jabber.org/protocol/muc#user'>
//				<decline from='user2@352.cn' result='1\2'>
//					<reason><![CDATA[我不想加入]]></reason>
//				</decline>
//			</x>
//		</message>
		Message msg = new Message();
		msg.setType(Message.Type.groupchat);
		msg.setTo(inviter);
		msg.setFrom(groupAsFrom(group.getId()));
		Element x = msg.addChildElement(TAG.X, NAMESPACE.MUC_USER);
		Element decline = x.addElement(TAG.DECLINE);
		decline.addAttribute(ATTRIBUTE_TAG.FROM, invitee);
		decline.addAttribute(ATTRIBUTE_TAG.RESULT, result);
		
		Element reason_e = decline.addElement(TAG.REASON);
		if (reason == null || "".equals(reason.trim()))
			reason = "";
		if (Constants.INVITE_DENIED.equals(result))
			reason_e.addCDATA(getJidNode(invitee) + "拒绝加入群[" + group.getGroup_name() + "].备注：" + reason);
		else if (Constants.INVITE_ACCEPT.equals(result))
			reason_e.addCDATA(getJidNode(invitee) + "加入群[" + group.getGroup_name() + "].备注：" + reason);
		return msg;
	}
	
//	/**
//	 * 生成群聊信息，发送给群成员
//	 * 
//	 * @param group_id
//	 * @param from
//	 * @param content
//	 * @return
//	 */
//	public Message createGroupMessage(long group_id, String from, String content) {
////		<message from='user1@352.cn' to='usern@352.cn' type='groupchat'>
////		    <group xmlns='http://jabber.org/protocol/muc' id='13' />
////		    <body><![CDATA[hello world.]]</body>
////		</message
//		Element msg_e = DocumentFactory.getInstance().createDocument().addElement("message");
//		Message msg = new Message(msg_e);
//		msg.setType(Message.Type.groupchat);
//		msg.setFrom(from);
//		Element group = msg_e.addElement(TAG.GROUP, NAMESPACE.MUC);
//		group.addAttribute(ATTRIBUTE_TAG.ID, String.valueOf(group_id));
//		Element body = msg_e.addElement(TAG.BODY);
//		body.addCDATA(content);
//		return msg;
//	}
	
	/**
	 * 生成私聊信息，发送给接收者 
	 * 
	 * @param sender
	 * @param receiver
	 * @param group_id
	 * @param content
	 * @return
	 */
	public Message createGroupPriMessage(String sender, String receiver, long group_id, String content) {
//		<message from='user1@352.cn' to='user2@352.cn' type='chat'>
//		    <group xmlns='http://jabber.org/protocol/muc' id='13' />
//		    <body><![CDATA[hello world.]]</body>
//		</message>
		Element msg_e = DocumentFactory.getInstance().createDocument().addElement("message");
		Message msg = new Message(msg_e);
		msg.setType(Message.Type.chat);
		msg.setFrom(sender);
		msg.setTo(receiver);
		Element group = msg_e.addElement(TAG.GROUP, NAMESPACE.MUC);
		group.addAttribute(ATTRIBUTE_TAG.ID, String.valueOf(group_id));
		Element body = msg_e.addElement(TAG.BODY);
		body.addCDATA(content);
		return msg;
	}
	
	/**
	 * from为group
	 * 
	 * @param group_id
	 * @return
	 */
	private String groupAsFrom(long group_id) {
		return group_id + "@" + serverDomain;
	}
	
	/**
	 * 返回jid的node
	 * 
	 * @param jid
	 * @return
	 */
	private String getJidNode(String jid) {
		return jid.split("@")[0];
	}
	
	/**
	 * 
	 * 线程池发送消息
	 * 
	 * @param packet
	 * @param componentManager
	 */
	public void sendPacket(final Packet packet, final ComponentManager componentManager) {
		sendPacketPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					componentManager.sendPacket(null, packet);
				} catch (ComponentException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 取组件服务域名
	 * 
	 * @return
	 */
	public String getServerDomain() {
		return serverDomain;
	}
}
