package com.p2p.www.common;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;
import com.p2p.www.admin.AdminController;
import com.p2p.www.admin.UserController;
import com.p2p.www.model.Youku_playlist;
import com.p2p.www.model.Youku_user;
import com.p2p.www.model.Youku_video;

/**
 * 
 * @author Administrator
 *
 */
public class CommonConfig extends JFinalConfig {
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile("config.properties");				
		me.setDevMode(getPropertyToBoolean("devMode", false));
		// 设置视图类型为Jsp，否则默认为FreeMarker
		me.setViewType(ViewType.JSP); 							
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add("/", CommonController.class);
		me.add("/admin", AdminController.class);
		me.add("/admin/user", UserController.class);
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
		me.add(c3p0Plugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		// 映射blog 表到 Blog模型
		arp.addMapping("youku_user", Youku_user.class);
		arp.addMapping("youku_video", Youku_video.class);
		arp.addMapping("youku_playlist", Youku_playlist.class);
		
		//定时任务
		QuartzPlugin qp = new QuartzPlugin();
		me.add(qp);
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
	}	
}
