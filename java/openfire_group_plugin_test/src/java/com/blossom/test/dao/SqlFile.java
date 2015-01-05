package com.blossom.test.dao;

import com.blossom.test.entity.GroupApply;

public class SqlFile {
	//get
	//��ѯ�û�����Ⱥ
	protected static final String GET_USER_GROUPS_BY_NAME = "select g.id, g.group_name, g.creator from tb_user_group g, tb_user_group_member m " +
			"where g.id = m.group_id and m.member_jid = ?";
	//����id��ѯȺ
	protected static final String GET_GROUP_BY_ID = "select id, group_name, creator from tb_user_group where id = ?";
	//����id��ѯ��Ⱥ����
//		protected static final String GET_GROUPS_APPLY_BY_ID = "select a.apply_user, g.creator, g.group_name, g.id from tb_user_group g, " +
//				"tb_user_group_apply a where a.id = ? and a.group_id = g.id";
	//����Ⱥid,�û������Ƿ������
	protected static final String GET_GROUPS_APPLY_BY_ID_NAME = "select id from tb_user_group_apply " +
			"where group_id = ? and apply_user = ? and status = '" + GroupApply.STATUS_NEW + "'";
	//����Ⱥid,�û������Ƿ���Ⱥ��
	protected static final String GET_GROUP_MEMBER_BY_ID_NAME = "select id, role from tb_user_group_member " +
			"where group_id = ? and member_jid = ?";
	//����Ⱥid,��Ⱥ��Ա
	protected static final String GET_GROUP_MEMBERS_BY_ID = "select member_jid, member_nick, role from tb_user_group_member " +
			"where group_id = ? order by role desc";
	//ȡδ�����Ⱥϵͳ��Ϣ
	protected static final String GET_GROUP_SYS_MESSAGE_BY_RECEIVER = "select id, content from tb_group_sys_message " +
			"where receiver = ? and status = ? order by create_date";
	//ȡ���û����е�Ⱥ��Ⱥ��Ա
	protected static final String GET_ALL_GROUP_MEMBERS_BY_NAME = "select m1.group_id, m1.member_jid, m1.member_nick, m1.role " +
			"from tb_user_group_member m1, tb_user_group_member m2 " +
			"where m1.group_id = m2.group_id and m2.member_jid = ?";
	
	//create
	//��Ⱥ
	protected static final String CREATE_GROUP = "insert into tb_user_group(creator, group_name) values (?, ?)";
	//���Ⱥ��Ա
	protected static final String CREATE_GROUP_MEMBER = "insert into tb_user_group_member(group_id, member_jid, member_nick, role) values(?,?,?,?)";
	//�����Ⱥ����
	protected static final String CREATE_GROUP_APPLY = "insert into tb_user_group_apply(group_id, apply_user) values(?,?)";
	//�����Ⱥ����ϵͳ��Ϣ
	protected static final String CREATE_GROUP_SYS_MESSAGE = "insert into tb_group_sys_message" +
			"(group_id, creator, receiver, content) values(?,?,?,?)";
	//��¼Ⱥ��Ϣ
	protected static final String CREATE_GROUP_MESSAGE = "insert into tb_user_group_message(group_id, sender, content) values(?,?,?)";
	//��¼Ⱥ˽����Ϣ
	protected static final String CREATE_GROUP_PRI_MESSAGE = "insert into tb_user_group_pri_message(sender, receiver, group_id, content)" +
			" values(?,?,?,?)";
	
	//update
	//������Ⱥ����
	protected static final String UPDATE_GROUP_APPLY = "update tb_user_group_apply set processe_date = now(), status = ? where id= ?";
	//����ϵͳ��Ϣ״̬
	protected static final String UPDATE_GROUP_APPLY_MESSAGE = "update tb_group_sys_message set processe_date = now(), status = ? " +
			"where creator = ? and group_id = ? and status = ?";
	//��Ⱥ����
	protected static final String UPDATE_GROUP_NAME_BY_ID = "update tb_user_group set group_name = ? where id = ?";
	
	//delete
	//ɾ��Ⱥ
	protected static final String DELETE_GROUP_BY_ID = "delete from tb_user_group where id = ?";
	//ɾ��Ⱥ��Ա
	protected static final String DELETE_GROUP_MEMBER_BY_GROUPID = "delete from tb_user_group_member where group_id = ?";
	//ɾ��Ⱥ����
	protected static final String DELETE_GROUP_APPLY_BY_GROUPID = "delete from tb_user_group_apply where group_id = ?";
	//ɾ��Ⱥ������Ϣ
	protected static final String DELETE_GROUP_SYS_MESSAGE_BY_GROUPID = "delete from tb_group_sys_message where group_id = ?";
	//ɾ��Ⱥ��Ա
	protected static final String DELETE_GROUP_MEMBER_BY_GROUPID_MEMBER_JID = "delete from tb_user_group_member where group_id = ? and member_jid = ?";
}
