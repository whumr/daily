package com.blossom.test.dao;

import org.jivesoftware.openfire.muc.MUCRole.Affiliation;

import com.blossom.test.entity.GroupApply;

public class SqlFile_new {

	//���Ⱥ
	protected static final String ADD_ROOM = 
			"insert into ofmucroom (serviceid, roomid, creationdate, modificationdate, name, naturalname, " +
	                "description, lockeddate, emptydate, canchangesubject, maxusers, publicroom, moderated, " +
	                "membersonly, caninvite, roompassword, candiscoverjid, logenabled, subject, " +
	                "rolestobroadcast, usereservednick, canchangenick, canregister, isdelete) values " +
	                "(?, ?, ?, ?, ?, ?," +
	                "?, '000000000000000', ?, 0, 100, 1, 0," +
	                "1, 1, ?, 1, 1, ?," +
	                "7, 0, 1, 1, 1)";
	
	//��ӽ�ɫ
	protected static final String ADD_AFFILIATION = "insert into ofmucaffiliation (roomid,jid,affiliation) values (?,?,?)";

	//���member
	protected static final String ADD_MEMBER = "insert into ofmucmember (roomid,jid,nickname) values (?,?,?)";
	
	//��ѯ�û�����Ⱥ
	protected static final String GET_USER_GROUPS_BY_NAME = "select r.roomid, r.name, a.jid " +
			"from ofmucroom r, ofmucaffiliation a, ofmucmember m " +
			"where r.roomid = a.roomid and r.roomid = m.roomid and m.jid = ? and r.serviceid = ? and r.isdelete = 0";
	
	//�����Ⱥ����
	protected static final String CREATE_GROUP_APPLY = "insert into tb_user_group_apply(group_id, apply_user) values(?,?)";
	
	//�����Ⱥ����ϵͳ��Ϣ
	protected static final String CREATE_GROUP_SYS_MESSAGE = "insert into tb_group_sys_message" +
			"(group_id, creator, receiver, content) values(?,?,?,?)";
	
	//����Ⱥid,�û������Ƿ���Ⱥ��
	protected static final String GET_GROUP_MEMBER_BY_ID_NAME = "select roomid from ofmucmember where roomid=? and jid=?";
	
	//����Ⱥid,�û������Ƿ������
	protected static final String GET_GROUPS_APPLY_BY_ID_NAME = "select id from tb_user_group_apply " +
			"where group_id = ? and apply_user = ? and status = '" + GroupApply.STATUS_NEW + "'";
	
	//����id��ѯȺ
	protected static final String GET_GROUP_BY_ID = "select r.roomid, r.name, a.jid from ofmucroom r, ofmucaffiliation a " +
			"where r.roomid = a.roomid and a.affiliation = " + Affiliation.owner.getValue() + " and r.roomid = ? and r.isdelete = 0";
	
	//������Ⱥ����
	protected static final String UPDATE_GROUP_APPLY = "update tb_user_group_apply set processe_date = now(), status = ? where id= ?";
	//����ϵͳ��Ϣ״̬
	protected static final String UPDATE_GROUP_APPLY_MESSAGE = "update tb_group_sys_message set processe_date = now(), status = ? " +
			"where creator = ? and group_id = ? and status = ?";
	
	//����Ⱥid,��Ⱥ��Ա
	protected static final String GET_GROUP_MEMBERS_BY_ID = "select m.jid, m.nickname, ifnull(a.affiliation, " 
			+ Affiliation.member.getValue() + ") " + "from ofmucaffiliation a right join ofmucmember m " +
			"on a.roomid = m.roomid and a.jid = m.jid where m.roomid = ?";
	
	//��¼Ⱥ��Ϣ
	protected static final String CREATE_GROUP_MESSAGE = "insert into tb_user_group_message(group_id, sender, content) values(?,?,?)";
	
	//ȡδ�����Ⱥϵͳ��Ϣ
	protected static final String GET_GROUP_SYS_MESSAGE_BY_RECEIVER = "select id, content from tb_group_sys_message " +
			"where receiver = ? and status = ? order by create_date";
	
	//delete
	//ɾ��Ⱥ
	protected static final String DELETE_GROUP_BY_ID = "update ofmucroom set isdelete = 1 where roomid = ?";
	//ɾ��Ⱥ��Ա
//	protected static final String DELETE_GROUP_MEMBER_BY_GROUPID = "delete from tb_user_group_member where group_id = ?";
	//ɾ��Ⱥ����
	protected static final String DELETE_GROUP_APPLY_BY_GROUPID = "delete from tb_user_group_apply where group_id = ?";
	//ɾ��Ⱥ������Ϣ
	protected static final String DELETE_GROUP_SYS_MESSAGE_BY_GROUPID = "delete from tb_group_sys_message where group_id = ?";
	
	//ɾ��Ⱥ��Ա
	protected static final String DELETE_GROUP_MEMBER_BY_GROUPID_MEMBER_JID = "delete from ofmucmember where roomid = ? and jid = ?";
	//ɾ����ɫ
	protected static final String DELETE_GROUP_AFFILIATION_BY_GROUPID_MEMBER_JID = "delete from ofmucaffiliation where roomid = ? and jid = ?";
	
	//ȡ���û����е�Ⱥ��Ⱥ��Ա
	protected static final String GET_ALL_GROUP_MEMBERS_BY_NAME = "select m.roomid, m.jid, m.nickname from ofmucmember m, ofmucmember m1 " +
			"where m.roomid = m1.roomid and m1.jid = ?";
	
	//��Ⱥ����
	protected static final String UPDATE_GROUP_NAME_BY_ID = "update ofmucroom set name = ?, naturalname = ? where roomid = ?";
	
	//��¼Ⱥ˽����Ϣ
	protected static final String CREATE_GROUP_PRI_MESSAGE = "insert into tb_user_group_pri_message(sender, receiver, group_id, content)" +
			" values(?,?,?,?)";

	//����Ⱥ״̬
	protected static final String UPDATE_GROUP_ISDELETE_BY_ID = "update ofmucroom set isdelete = 0 where roomid = ?";
}
