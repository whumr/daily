package com.blossom.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.muc.MUCRole;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatService;
import org.jivesoftware.util.cache.Cache;
import org.jivesoftware.util.cache.CacheFactory;
import org.jivesoftware.util.cache.CacheWrapper;
import org.xmpp.packet.JID;

public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = -651910257170233530L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		StringBuilder buffer = new StringBuilder();
		for (MultiUserChatService mucService : XMPPServer.getInstance().getMultiUserChatManager().getMultiUserChatServices()) {
			buffer.append(mucService.getName()).append(":")
				.append(mucService.getNumberChatRooms()).append(" rooms;")
				.append(mucService.getNumberConnectedUsers(false)).append(" users")
				.append(mucService.getNumberConnectedUsers(true)).append(" local users<br/>");
			buffer.append("<table><tr><td>name</td><td>Description</td><td>members</td><td>roles</td></tr>");
			for (MUCRoom room : mucService.getChatRooms()) {
				Collection<JID> members = room.getMembers();
				Collection<MUCRole> roles = room.getOccupants();
				
            	buffer.append("<tr><td>").append(room.getName())
            		.append("</td><td>").append(room.getDescription())
            		.append("</td><td>");
            	for (JID jid : members) {
					buffer.append(jid.toFullJID()).append(",");
				}
            	buffer.append("</td><td>");
            	for (MUCRole role : roles) {
					buffer.append(role.getNickname()).append("-")
						.append(role.getNodeID().toString()).append("-")
						.append(role.getRole().getValue()).append(",");
				}
            	buffer.append("</td></tr><br/>");
            }
		}
		
		Map<String, Cache> caches = CacheFactory.caches;
		List<String> localOnly = CacheFactory.localOnly;
		
		buffer.append("<br/><br/><table><tr><td>key</td><td>type</td><td>instance of</td><td>size</td><td>class</td></tr>");
		
		for (String key : caches.keySet()) {
			Cache cache = caches.get(key);
			if (cache.size() > 0) {
				Class c = null;
				if (!cache.isEmpty()) {
					Object o = cache.keySet().iterator().next();
					if (cache.get(o) != null)
						c = cache.get(o).getClass();
				}
				String class_name = c == null ? "unknown" : c.getName();
				
				buffer.append("<tr><td>").append(key).append("</td><td>").append(localOnly.contains(key) ? "local" : "remote")
				.append("</td><td>").append(((CacheWrapper)cache).getWrappedCache().getClass().getName()).append("</td><td>")
				.append(cache.size()).append("</td><td>").append(class_name).append("</td></tr>");
			}
		}
		buffer.append("</table>");
		
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.print("«Î«ÛSampleServlet POST Method" + req.getPathInfo() + "<br/>");
		out.print(buffer.toString());
		out.flush();
	}
}
