package com.blossom.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.muc.MUCRole.Affiliation;
import org.jivesoftware.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blossom.muc.LocalGroup;
import com.blossom.test.component.Constants;
import com.blossom.test.component.Constants.ATTRIBUTE_TAG;
import com.blossom.test.entity.Group;
import com.blossom.test.entity.GroupApply;
import com.blossom.test.entity.GroupMember;
import com.blossom.test.entity.GroupMessage;
import com.blossom.test.entity.GroupPriMessage;
import com.blossom.test.entity.GroupSysMessage;

public class GroupDbManager {

	private static final Logger Log = LoggerFactory.getLogger(GroupDbManager.class);
	
	private static GroupDbManager userGroupDbManager;
	
	private GroupDbManager() {
	}
	
	public static GroupDbManager getInstance() {
		if (userGroupDbManager == null)
			userGroupDbManager = new GroupDbManager();
		return userGroupDbManager;
	}
	
	/**
	 * 插入群
	 * @param group
	 * @return
	 * @throws SQLException
	 */
	public long insertGroup(LocalGroup group) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(SqlFile_new.ADD_ROOM);
			
//			"insert into ofmucroom (serviceid, roomid, creationdate, modificationdate, name, naturalname, " +
//            "description, lockeddate, emptydate, canchangesubject, maxusers, publicroom, moderated, " +
//            "membersonly, caninvite, roompassword, candiscoverjid, logenabled, subject, " +
//            "rolestobroadcast, usereservednick, canchangenick, canregister) values " +
//            "(?, ?, ?, ?, ?, ?," +
//            "?, '000000000000000', ?, 0, 100, 1, 0," +
//            "1, 1, ?, 1, 1, ?," +
//            "7, 0, 1, 1)";
			Date date = new Date();
			pstmt.setLong(1, XMPPServer.getInstance().getMultiUserChatManager().getMultiUserChatServiceID(group.getMUCService().getServiceName()));
            pstmt.setLong(2, group.getID());
            pstmt.setString(3, StringUtils.dateToMillis(date));
            pstmt.setString(4, StringUtils.dateToMillis(date));
            pstmt.setString(5, group.getName());
            pstmt.setString(6, group.getName());
            pstmt.setString(7, group.getDescription());
            Date emptyDate = group.getEmptyDate();
            if (emptyDate == null) {
                pstmt.setString(8, null);
            }
            else {
                pstmt.setString(8, StringUtils.dateToMillis(emptyDate));
            }
            pstmt.setString(9, group.getPassword());
            pstmt.setString(10, group.getSubject());
			pstmt.executeUpdate();
			
			//取群id
			long id = group.getID();
			String creator = group.getOwner();
			
			//插入到群角色
			pstmt = con.prepareStatement(SqlFile_new.ADD_AFFILIATION);
            pstmt.setLong(1, id);
            pstmt.setString(2, creator);
            pstmt.setInt(3, Affiliation.owner.getValue());
            pstmt.executeUpdate();
            
			//插入到群成员
            pstmt = con.prepareStatement(SqlFile_new.ADD_MEMBER);
            pstmt.setLong(1, id);
            pstmt.setString(2, creator);
            pstmt.setString(3, creator.split("@")[0]);
            pstmt.executeUpdate();
			con.commit();
			
			Log.info("create user group succeed...");
			return id;
		} catch (SQLException sqle) {
			Log.error("create user group fail...", sqle);
			throw sqle;
		} finally {
			DbConnectionManager.closeConnection(rs, pstmt, con);
		}
	}
	
	/**
	 * 查找群
	 * 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public PageList<Group> searchGroups(Map<String, String> map, long service_id) throws SQLException {
		PageList<Group> list = new PageList<Group>();
		//拼接sql
		StringBuilder count_sql = new StringBuilder("select count(*) from ofmucroom where serviceid = ")
			.append(service_id).append(" and ");
		StringBuilder sql = new StringBuilder("select roomid, name from ofmucroom where serviceid = ")
			.append(service_id).append(" and ");
		StringBuilder condition_sql = new StringBuilder();
		StringBuilder page_sql = new StringBuilder();
		if (map.containsKey("id"))
			condition_sql.append("roomid = ").append(map.get("id"));
		else {
			if (map.containsKey("name")) {
				condition_sql.append("name like '%")
					.append(map.get("name").replaceAll("'", "''")).append("%'");
			}
			int page = PageList.PAGE, page_size = PageList.PAGE_SIZE;
			if (map.containsKey("page"))
				page = Integer.parseInt(map.get("page"));
			if (map.containsKey("page_size"))
				page_size = Integer.parseInt(map.get("page_size"));
			page_sql.append(" order by roomid limit ").append((page - 1) * page_size)
				.append(", ").append(page_size);
			list.setPage(page);
			list.setPage_size(page_size);
		}
		//查询
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DbConnectionManager.getConnection();
            //根据id查找
            if (map.containsKey("id")) {
	            pstmt = con.prepareStatement(sql.append(condition_sql).toString());
	            rs = pstmt.executeQuery();
	            while(rs.next()) {
	            	Group group = new Group();
	            	group.setId(rs.getLong(1));
	            	group.setGroup_name(rs.getString(2));
	            	list.add(group);
	            }
	            list.setCount(list.size());
	        //根据条件查找
            } else {
            	//查总记录数
            	pstmt = con.prepareStatement(count_sql.append(condition_sql).toString());
	            rs = pstmt.executeQuery();
	            if (rs.next()) {
	            	list.setCount(rs.getInt(1));
	            }
	            //有结果
	            if (list.getCount() > 0) {
	            	pstmt = con.prepareStatement(sql.append(condition_sql).append(page_sql).toString());
	            	rs = pstmt.executeQuery();
		            while(rs.next()) {
		            	Group group = new Group();
		            	group.setId(rs.getLong(1));
		            	group.setGroup_name(rs.getString(2));
		            	list.add(group);
		            }
	            }
            }
            return list;
        } catch (SQLException sqle) {
            Log.error("get user groups fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(rs, pstmt, con);
        }
	}
	
	/**
	 * 取用户所在的群
	 * @param user_name
	 * @return
	 * @throws SQLException
	 */
	public List<Group> getGroupsByUserName(String user_name, long service_id) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SqlFile_new.GET_USER_GROUPS_BY_NAME);
            pstmt.setString(1, user_name);
            pstmt.setLong(2, service_id);
            rs = pstmt.executeQuery();
            List<Group> list = new ArrayList<Group>();
            while(rs.next()) {
            	Group group = new Group();
            	group.setId(rs.getLong(1));
            	group.setGroup_name(rs.getString(2));
            	group.setCreator(rs.getString(3));
            	list.add(group);
            }
            return list;
        } catch (SQLException sqle) {
            Log.error("get user groups fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(rs, pstmt, con);
        }
	}
	
	/**
	 * 插入群成员
	 * @param member
	 * @throws SQLException
	 */
	public void insertGroupMember(GroupMember member) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            long group_id = member.getGroup_id();
            String jid = member.getMember_jid();
            //插入到群角色
//            pstmt = con.prepareStatement(SqlFile_new.ADD_AFFILIATION);
//            pstmt.setLong(1, group_id);
//            pstmt.setString(2, jid);
//            pstmt.setInt(3, Affiliation.member.getValue());
//            pstmt.executeUpdate();
            
			//插入到群成员
            pstmt = con.prepareStatement(SqlFile_new.ADD_MEMBER);
            pstmt.setLong(1, group_id);
            pstmt.setString(2, jid);
            pstmt.setString(3, member.getMember_nick());
            pstmt.executeUpdate();
            
            Log.info("create user group member succeed...");
        } catch (SQLException sqle) {
            Log.error("create user group member fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
	}
	
	/**
	 * 插入入群申请
	 * 
	 * @param apply
	 * @return
	 * @throws SQLException
	 */
	public long insertGroupApply(GroupApply apply) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        long id = -1;
        try {
            con = DbConnectionManager.getConnection();
            long group_id = apply.getGroup_id();
            String applier = apply.getApply_user();
            //插入申请记录
            pstmt = con.prepareStatement(SqlFile_new.CREATE_GROUP_APPLY);
            pstmt.setLong(1, group_id);
            pstmt.setString(2, applier);
            pstmt.executeUpdate();
            //取申请id
            rs = pstmt.getGeneratedKeys();
            if (rs.next())
            	id = rs.getLong(1);
            
            Log.info("create user group apply succeed...");
            return id;
        } catch (SQLException sqle) {
            Log.error("create user group apply fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
	}
	
	/**
	 * 插入系统消息
	 * 
	 * @param sysMessage
	 * @return
	 * @throws SQLException
	 */
	public long insertGroupSysMessage(GroupSysMessage sysMessage) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        long id = -1;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SqlFile_new.CREATE_GROUP_SYS_MESSAGE);
          pstmt.setLong(1, sysMessage.getGroup_id());
          pstmt.setString(2, sysMessage.getCreator());
          pstmt.setString(3, sysMessage.getReceiver());
          pstmt.setString(4, sysMessage.getContent());
          pstmt.executeUpdate();
          //取消息id
          rs = pstmt.getGeneratedKeys();
          if (rs.next())
          	id = rs.getLong(1);
        Log.info("create user group sys message succeed...");
        return id;
        } catch (SQLException sqle) {
            Log.error("create user group sys message fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
	}
	
	/**
	 * 查看用户是否是群成员
	 * 
	 * @param group_id
	 * @param member_jid
	 * @return
	 */
	public boolean isGroupMember(long group_id, String member_jid) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DbConnectionManager.getConnection();
            //检查是否已经在群中,是否已经发送过入群申请
            pstmt = con.prepareStatement(SqlFile_new.GET_GROUP_MEMBER_BY_ID_NAME);
            pstmt.setLong(1, group_id);
            pstmt.setString(2, member_jid);
            rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException sqle) {
            Log.error("check user in group fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
	}
	
	/**
	 * 删除群成员
	 * 
	 * @param group_id
	 * @param member_jid
	 * @return
	 * @throws SQLException
	 */
	public int deleteGroupMember(long group_id, String member_jid) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            //删除角色
            pstmt = con.prepareStatement(SqlFile_new.DELETE_GROUP_AFFILIATION_BY_GROUPID_MEMBER_JID);
            pstmt.setLong(1, group_id);
            pstmt.setString(2, member_jid);
            pstmt.executeUpdate();
            //删除群成员
            pstmt = con.prepareStatement(SqlFile_new.DELETE_GROUP_MEMBER_BY_GROUPID_MEMBER_JID);
            pstmt.setLong(1, group_id);
            pstmt.setString(2, member_jid);
            return pstmt.executeUpdate();
        } catch (SQLException sqle) {
            Log.error("delete group member fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
	}
	
	/**
	 * 获取用户的入群申请
	 * 
	 * @param group_id
	 * @param member_jid
	 * @return
	 * @throws SQLException
	 */
	public GroupApply getGroupApplyByUserGroupId(long group_id, String member_jid) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DbConnectionManager.getConnection();
            //检查是否已经申请过
            pstmt = con.prepareStatement(SqlFile_new.GET_GROUPS_APPLY_BY_ID_NAME);
            pstmt.setLong(1, group_id);
            pstmt.setString(2, member_jid);
            rs = pstmt.executeQuery();
            //用户已经在群中
            if (rs.next()) {
            	GroupApply apply = new GroupApply(group_id, member_jid);
            	apply.setId(rs.getLong(1));
            	return apply;
            } else 
            	return null;
        } catch (SQLException sqle) {
            Log.error("check user in group fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
	}
	
	/**
	 * 根据id取群信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public Group getGroupById(long id) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SqlFile_new.GET_GROUP_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            Group group = null;
            if (rs.next()) {
            	group = new Group();
            	group.setId(rs.getLong(1));
            	group.setGroup_name(rs.getString(2));
            	group.setCreator(rs.getString(3));
            }
            return group;
        } catch (SQLException sqle) {
            Log.error("get user groups fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(rs, pstmt, con);
        }
	}
	
	/**
	 * 处理入群申请
	 * 
	 * @param creator
	 * @param applier
	 * @param apply_id
	 * @param result
	 * @return
	 * @throws SQLException
	 */
	public void processGroupApply(GroupApply apply) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            //更新申请状态
            pstmt = con.prepareStatement(SqlFile_new.UPDATE_GROUP_APPLY);
            pstmt.setString(1, apply.getStatus());
            pstmt.setLong(2, apply.getId());
            pstmt.executeUpdate();
            //更新系统消息状态
            pstmt = con.prepareStatement(SqlFile_new.UPDATE_GROUP_APPLY_MESSAGE);
            pstmt.setString(1, GroupSysMessage.STATUS_DONE);
            pstmt.setString(2, apply.getApply_user());
            pstmt.setLong(3, apply.getGroup_id());
            pstmt.setString(4, GroupSysMessage.STATUS_NEW);
            pstmt.executeUpdate();
            
            //如果是同意,将申请者加入到群中
            if (GroupApply.STATUS_ACCEPT.equals(apply.getStatus())) {
    			//插入到群成员
                pstmt = con.prepareStatement(SqlFile_new.ADD_MEMBER);
                pstmt.setLong(1, apply.getGroup_id());
                pstmt.setString(2, apply.getApply_user());
                pstmt.setString(3, apply.getApply_user().split("@")[0]);
                pstmt.executeUpdate();
            }
            Log.info("update user group apply succeed...");
        } catch (SQLException sqle) {
            Log.error("update user group apply fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
	}
	
	/**
	 * 根据群id取群成员
	 * @param group_id
	 * @return
	 * @throws SQLException
	 */
	public List<GroupMember> listGroupMembers(long group_id) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SqlFile_new.GET_GROUP_MEMBERS_BY_ID);
            pstmt.setLong(1, group_id);
            rs = pstmt.executeQuery();
            List<GroupMember> list = new ArrayList<GroupMember>();
            while (rs.next()) {
            	list.add(new GroupMember(rs.getString(1),
            			rs.getString(2), rs.getString(3)));
            }
            return list;
        } catch (SQLException sqle) {
            Log.error("get user groups fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(rs, pstmt, con);
        }
	}
	
	/**
	 * 插入群消息
	 * @param message
	 * @throws SQLException
	 */
	public void insertGroupMessage(GroupMessage message) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SqlFile_new.CREATE_GROUP_MESSAGE);
            pstmt.setLong(1, message.getGroup_id());
            pstmt.setString(2, message.getSender());
            pstmt.setString(3, message.getContent());
            pstmt.executeUpdate();
            Log.info("create group message succeed...");
        } catch (SQLException sqle) {
            Log.error("create group message fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
	}
	
	/**
	 * 获取未发送的系统消息
	 * @param receiver
	 * @return
	 * @throws SQLException
	 */
	public List<GroupSysMessage> getUnsendSysMessage(String receiver) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SqlFile_new.GET_GROUP_SYS_MESSAGE_BY_RECEIVER);
            pstmt.setString(1, receiver);
            pstmt.setString(2, GroupSysMessage.STATUS_NEW);
            rs = pstmt.executeQuery();
            List<GroupSysMessage> list = new ArrayList<GroupSysMessage>();
            while (rs.next()) {
            	GroupSysMessage sys_message = new GroupSysMessage();
            	sys_message.setId(rs.getLong(1));
            	sys_message.setContent(rs.getString(2));
            	list.add(sys_message);
            }
            return list;
        } catch (SQLException sqle) {
            Log.error("get sys message fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(rs, pstmt, con);
        }
	}
	
	/**
	 * 删除群
	 * @param group_id
	 * @throws SQLException
	 */
	public void deleteGroupById(long group_id) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            //删除群
            pstmt = con.prepareStatement(SqlFile_new.DELETE_GROUP_BY_ID);
            pstmt.setLong(1, group_id);
            pstmt.executeUpdate();
            //删除群成员
//            pstmt = con.prepareStatement(SqlFile_new.DELETE_GROUP_MEMBER_BY_GROUPID);
//            pstmt.setLong(1, group_id);
//            pstmt.executeUpdate();
            //删除申请
            pstmt = con.prepareStatement(SqlFile_new.DELETE_GROUP_APPLY_BY_GROUPID);
            pstmt.setLong(1, group_id);
            pstmt.executeUpdate();
            //删除系统消息
            pstmt = con.prepareStatement(SqlFile_new.DELETE_GROUP_SYS_MESSAGE_BY_GROUPID);
            pstmt.setLong(1, group_id);
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            Log.error("delete group fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
	}
	
	/**
	 * 获取用户所在的所有群的成员，除了自己
	 * 
	 * @param member_jid
	 * @return
	 * @throws SQLException
	 */
	public List<GroupMember> getAllGroupMemberByMember(String member_jid) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SqlFile_new.GET_ALL_GROUP_MEMBERS_BY_NAME);
            pstmt.setString(1, member_jid);
            rs = pstmt.executeQuery();
            List<GroupMember> list = new ArrayList<GroupMember>();
            while (rs.next()) {
            	GroupMember member = new GroupMember(rs.getLong(1), 
            			rs.getString(2), rs.getString(3));
            	list.add(member);
            }
            return list;
        } catch (SQLException sqle) {
            Log.error("get all members fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(rs, pstmt, con);
        }
	}
	
	/**
	 * 取群历史消息
	 * 
	 * @param group_id
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<GroupMessage> listGroupHisMessage(long group_id, Map<String, String> params, long time) throws SQLException {
//		ATTRIBUTE_TAG.MAXSTANZAS ATTRIBUTE_TAG.SECONDS ATTRIBUTE_TAG.SINCE ATTRIBUTE_TAG.MAXCHARS);
		String sql = null;
		int maxchars = 0;
		if (params.isEmpty()) {
			sql = "select sender, content, create_time from tb_user_group_message order by id desc limit 0, 5";
		} else {
			StringBuilder buffer = new StringBuilder("select sender, content, create_time from tb_user_group_message "); 
			long seconds = 0, since = 0;
			//seconds
			if (params.containsKey(ATTRIBUTE_TAG.SECONDS)) {
				try {
					seconds = Long.parseLong(params.get(ATTRIBUTE_TAG.SECONDS)) * 1000L;
				} catch (NumberFormatException e) {
				}
			}
			//since
			if (params.containsKey(ATTRIBUTE_TAG.SINCE)) {
				try {
					since = Constants.DEFAULT_SDF.parse(params.get(ATTRIBUTE_TAG.SINCE)).getTime();
				} catch (ParseException e) {
				}
			}
			//maxchars
			if (params.containsKey(ATTRIBUTE_TAG.MAXCHARS)) {
				try {
					maxchars = Integer.parseInt(params.get(ATTRIBUTE_TAG.MAXCHARS));
				} catch (NumberFormatException e) {
				}
			}
			//取最近时间
			if (seconds > 0)
				seconds = time - seconds;
			since = Math.max(seconds, since);
			if (since > 0)
				buffer.append("where create_time > '")
					.append(Constants.DEFAULT_SDF.format(new Date(since))).append("' ");
			//有最大条数限制
			if (params.containsKey(ATTRIBUTE_TAG.MAXSTANZAS));
				buffer.append("limit 0, ").append(params.get(ATTRIBUTE_TAG.MAXSTANZAS));
			sql = buffer.toString();
		}
		//查询
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<GroupMessage> list = new ArrayList<GroupMessage>();
            int chars = 0;
            while (rs.next() && (chars < maxchars || maxchars <= 0)) {
            	GroupMessage msg = new GroupMessage(group_id, rs.getString(1), rs.getString(2));
            	msg.setCreate_time(rs.getDate(3));
            	list.add(msg);
            	chars += msg.getContent().getBytes().length;
            }
            return list;
        } catch (SQLException sqle) {
            Log.error("get his messages fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(rs, pstmt, con);
        }
	}
	
	/**
	 * 群改名
	 * 
	 * @param group_id
	 * @param new_name
	 * @throws SQLException
	 */
	public void renameGroup(long group_id, String new_name) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SqlFile_new.UPDATE_GROUP_NAME_BY_ID);
            pstmt.setString(1, new_name);
            pstmt.setString(2, new_name);
            pstmt.setLong(3, group_id);
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            Log.error("get all members fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
	}
	
	/**
	 * 插入群私聊消息
	 * 
	 * @param priMessage
	 * @return
	 * @throws SQLException
	 */
	public void insertGroupPriMessage(GroupPriMessage priMessage) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SqlFile_new.CREATE_GROUP_PRI_MESSAGE);
            pstmt.setString(1, priMessage.getSender());
            pstmt.setString(2, priMessage.getReceiver());
            pstmt.setLong(3, priMessage.getGroup_id());
            pstmt.setString(4, priMessage.getContent());
            pstmt.executeUpdate();
            Log.info("create group message succeed...");
        } catch (SQLException sqle) {
            Log.error("create group message fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
	}
	
	/**
	 * 设置群
	 * @param group
	 * @throws SQLException
	 */
	public void updateGroupIsdelete(LocalGroup group) throws SQLException {
		Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SqlFile_new.UPDATE_GROUP_ISDELETE_BY_ID);
            pstmt.setLong(1, group.getID());
            pstmt.executeUpdate();
            Log.info("update group message succeed...");
        } catch (SQLException sqle) {
            Log.error("create group message fail...", sqle);
            throw sqle;
        } finally {
            DbConnectionManager.closeConnection(pstmt, con);
        }
	}
}