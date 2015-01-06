package com.p2p.www.common;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * CommonController
 * @author Administrator
 *
 */
public class CommonController extends Controller {
	
	public void index() {
		Integer page = getParaToInt();
		Page<Record> videos = Db.paginate(page == null ? 1 : page, Common_Constants.PAGE_SIZE, 
				"select v.id, v.y_id, v.title, u.name user_name, v.view_count, v.up_count, v.down_count, v.published ", 
				"from youku_video v, youku_user u where v.user_id = u.id order by published desc");
		setAttr("videos", videos);
		render("/videos/index.jsp");
	}
}
