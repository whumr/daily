package com.blossom.test.dao;

import org.jivesoftware.openfire.muc.MUCRole.Affiliation;

import com.blossom.test.entity.GroupApply;

public class SqlFile_new {

	//添加群
	protected static final String ADD_ROOM = 
			"insert into ofmucroom (serviceid, roomid, creationdate, modificationdate, name, naturalname, " +
	                "description, lockeddate, emptydate, canchangesubject, maxusers, publicroom, moderated, " +
	                "membersonly, caninvite, roompassword, candiscoverjid, logenabled, subject, " +
	                "rolestobroadcast, usereservednick, canchangenick, canregister, isdelete) values " +
	                "(?, ?, ?, ?, ?, ?," +
	                "?, '000000000000000', ?, 0, 100, 1, 0," +
	                "1, 1, ?, 1, 1, ?," +
	                "7, 0, 1, 1, 1)";
	
	//添加角色
	protected static final String ADD_AFFILIATION = "insert into ofmucaffiliation (roomid,jid,affiliation) values (?,?,?)";

	//添加member
	protected static final String ADD_MEMBER = "insert into ofmucmember (roomid,jid,nickname) values (?,?,?)";
	
	//查询用户所在群
	protected static final String GET_USER_GROUPS_BY_NAME = "select r.roomid, r.name, a.jid " +
			"from ofmucroom r, ofmucaffiliation a, ofmucmember m " +
			"where r.roomid = a.roomid and r.roomid = m.roomid and m.jid = ? and r.serviceid = ? and r.isdelete = 0";
	
	//添加入群申请
	protected static final String CREATE_GROUP_APPLY = "insert into tb_user_group_apply(group_id, apply_user) values(?,?)";
	
	//添加入群申请系统消息
	protected static final String CREATE_GROUP_SYS_MESSAGE = "insert into tb_group_sys_message" +
			"(group_id, creator, receiver, content) values(?,?,?,?)";
	
	//根据群id,用户名查是否在群中
	protected static final String GET_GROUP_MEMBER_BY_ID_NAME = "select roomid from ofmucmember where roomid=? and jid=?";
	
	//根据群id,用户名查是否申请过
	protected static final String GET_GROUPS_APPLY_BY_ID_NAME = "select id from tb_user_group_apply " +
			"where group_id = ? and apply_user = ? and status = '" + GroupApply.STATUS_NEW + "'";
	
	//根据id查询群
	protected static final String GET_GROUP_BY_ID = "select r.roomid, r.name, a.jid from ofmucroom r, ofmucaffiliation a " +
			"where r.roomid = a.roomid and a.affiliation = " + Affiliation.owner.getValue() + " and r.roomid = ? and r.isdelete = 0";
	
	//更新入群申请
	protected static final String UPDATE_GROUP_APPLY = "update tb_user_group_apply set processe_date = now(), status = ? where id= ?";
	//更新系统消息状态
	protected static final String UPDATE_GROUP_APPLY_MESSAGE = "update tb_group_sys_message set processe_date = now(), status = ? " +
			"where creator = ? and group_id = ? and status = ?";
	
	//根据群id,查群成员
	protected static final String GET_GROUP_MEMBERS_BY_ID = "select m.jid, m.nickname, ifnull(a.affiliation, " 
			+ Affiliation.member.getValue() + ") " + "from ofmucaffiliation a right join ofmucmember m " +
			"on a.roomid = m.roomid and a.jid = m.jid where m.roomid = ?";
	
	//记录群消息
	protected static final String CREATE_GROUP_MESSAGE = "insert into tb_user_group_message(group_id, sender, content) values(?,?,?)";
	
	//取未处理的群系统信息
	protected static final String GET_GROUP_SYS_MESSAGE_BY_RECEIVER = "select id, content from tb_group_sys_message " +
			"where receiver = ? and status = ? order by create_date";
	
	//delete
	//删除群
	protected static final String DELETE_GROUP_BY_ID = "update ofmucroom set isdelete = 1 where roomid = ?";
	//删除群成员
//	protected static final String DELETE_GROUP_MEMBER_BY_GROUPID = "delete from tb_user_group_member where group_id = ?";
	//删除群申请
	protected static final String DELETE_GROUP_APPLY_BY_GROUPID = "delete from tb_user_group_apply where group_id = ?";
	//删除群申请消息
	protected static final String DELETE_GROUP_SYS_MESSAGE_BY_GROUPID = "delete from tb_group_sys_message where group_id = ?";
	
	//删除群成员
	protected static final String DELETE_GROUP_MEMBER_BY_GROUPID_MEMBER_JID = "delete from ofmucmember where roomid = ? and jid = ?";
	//删除角色
	protected static final String DELETE_GROUP_AFFILIATION_BY_GROUPID_MEMBER_JID = "delete from ofmucaffiliation where roomid = ? and jid = ?";
	
	//取出用户所有的群的群成员
	protected static final String GET_ALL_GROUP_MEMBERS_BY_NAME = "select m.roomid, m.jid, m.nickname from ofmucmember m, ofmucmember m1 " +
			"where m.roomid = m1.roomid and m1.jid = ?";
	
	//改群名称
	protected static final String UPDATE_GROUP_NAME_BY_ID = "update ofmucroom set name = ?, naturalname = ? where roomid = ?";
	
	//记录群私聊消息
	protected static final String CREATE_GROUP_PRI_MESSAGE = "insert into tb_user_group_pri_message(sender, receiver, group_id, content)" +
			" values(?,?,?,?)";

	//更新群状态
	protected static final String UPDATE_GROUP_ISDELETE_BY_ID = "update ofmucroom set isdelete = 0 where roomid = ?";
}
