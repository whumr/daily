package com.blossom.test;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;

import com.blossom.muc.GroupServiceImpl;

public class TestPlugin implements Plugin {

	private static final String LOAD_SERVICES = "SELECT subdomain,description,isHidden FROM ofMucService where isHidden = 1";
	
	private ConcurrentHashMap<String, GroupServiceImpl> groupServices = new ConcurrentHashMap<String, GroupServiceImpl>();
	
	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		//×¢²áÈºÁÄ×é¼þ
		
		loadServices();
		for (GroupServiceImpl groupService : groupServices.values()) {
			XMPPServer.getInstance().getMultiUserChatManager().registerMultiUserChatService(groupService);
//				ComponentManagerFactory.getComponentManager().addComponent(groupService.getServiceName(), groupService);
		}
		
//		GroupServiceImpl mucService = new GroupServiceImpl("group", "test muc", false);
		
//		try {
//			GroupComponent userGroupComponent = GroupComponent.getInstance();
//			ComponentManagerFactory.getComponentManager().addComponent(userGroupComponent.getName(), userGroupComponent);
//		} catch (ComponentException e) {
//			System.out.println("add UserGroupComponent fail...");
//			e.printStackTrace();
//		}
		
		System.out.println("test_plugin initialized...");
	}

	@Override
	public void destroyPlugin() {
		System.out.println("test_plugin destroyed...");
	}

	private void loadServices() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_SERVICES);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String subdomain = rs.getString(1);
				String description = rs.getString(2);
				Boolean isHidden = Boolean.valueOf(rs.getString(3));
				GroupServiceImpl muc = new GroupServiceImpl(subdomain, description, isHidden);
				groupServices.put(subdomain, muc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.closeConnection(rs, pstmt, con);
		}
	}
}
