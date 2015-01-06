package com.p2p.www.admin;

import com.p2p.www.common.BaseController;

/**
 * 用户管理
 * @author Administrator
 *
 */
public class AdminController extends BaseController {

	/**
	 * 后台管理首页
	 */
	public void index() {
		redirect("/admin/user");
	}
}