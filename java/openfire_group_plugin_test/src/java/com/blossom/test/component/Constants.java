package com.blossom.test.component;

import java.text.SimpleDateFormat;

public class Constants {
	
	public static final String COMPONENT_NAME_KEY = "plugin.group.serviceName";
	public static final String DEFAULT_COMPONENT_NAME = "group";

	public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final String INVITE_ACCEPT = "1";
	public static final String INVITE_DENIED = "2";
	
	public static final String CODE_NEWGROUP = "201";
	public static final String CODE_NEWMEMBER = "301";
	public static final String CODE_KICKMEMBER = "307";
	public static final String CODE_QUITEMBER = "308";

	public static final class TAG {
		public static final String X = "x";
		public static final String BODY = "body";
		public static final String QUERY = "query";
		public static final String ITEM = "item";
		public static final String ERROR = "errormessage";
		public static final String REASON = "reason";
		public static final String DESTROY = "destroy";
		public static final String CONDITION = "condition";
		public static final String SET = "set";
		public static final String QUIT = "quit";
		public static final String HISTORY = "history";
		public static final String DELAY = "delay";
		public static final String SUBJECT = "subject";
		public static final String INVITE = "invite";
		public static final String DECLINE = "decline";
		public static final String GROUP = "group";
		//申请人
		public static final String APPLIER = "applier";
		//申请处理结果
		public static final String RESULT = "result";
		//presence status
		public static final String STATUS = "status";
	}

	public static final class ATTRIBUTE_TAG {
		public static final String JID = "jid";
		public static final String ID = "id";
		public static final String NAME = "name";
		//错误代码
		public static final String CODE = "code";
		//错误类型
		public static final String TYPE = "type";
		//请求群成员
		public static final String AFFILIATION = "affiliation";
		//成员昵称
		public static final String NICK = "nick";
		//成员角色
		public static final String ROLE = "role";
		//查询结果集
		public static final String INDEX = "index";
		//是否在线
		public static final String ONLINE = "online";
		
		//查询结果分页
		public static final String PAGE = "page";
		public static final String PAGESIZE = "pagesize";
		public static final String COUNT = "count";
		
		//历史消息
		//最大返回条数
		public static final String MAXSTANZAS = "maxstanzas";
		//最后几分钟
		public static final String SECONDS = "seconds";
		//起始时间
		public static final String SINCE = "since";
		//最大字符数
		public static final String MAXCHARS = "maxchars";
		//时间戳
		public static final String STAMP = "stamp";
		//邀请用户加入群
		public static final String FROM = "from";
		public static final String TO = "to";
		public static final String RESULT = "result";
	}
	
	public static final class ATTRIBUTE_VALUE {
		public static final String X_TYPE_SUBMIT = "submit";
		public static final String MEMBER = "member";
		public static final String OWNER = "owner";
		public static final String NONE = "none";
	}
	
	public static final class NAMESPACE {
		//查询群
		public static final String DISCO_ITEMS = "http://jabber.org/protocol/disco#items";
		//创建群
		public static final String MUC_OWNER = "http://jabber.org/protocol/muc#owner";
		//muc用户
		public static final String MUC_USER = "http://jabber.org/protocol/muc#user";
		//申请加入群
		public static final String JABBER_REGISTER = "jabber:iq:register";
		//创建群data
		public static final String JABBER_X_DATA = "jabber:x:data";
		//查询群成员
		public static final String MUC_ADMIN = "http://jabber.org/protocol/muc#admin";
		//结果集
		public static final String JABBER_RSM = "http://jabber.org/protocol/rsm";
		//MUC
		public static final String MUC = "http://jabber.org/protocol/muc";
		//delay
		public static final String URN_XMPP_DELAY = "urn:xmpp:delay";
		//error
		public static final String ERROR = "jabber:error";
	}
	
}
