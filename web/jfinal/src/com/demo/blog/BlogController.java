package com.demo.blog;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * BlogController
 * 注意：在实际项目中业务与sql需要写在Model中，此demo仅为示意,故将sql写在了Controller中
 */
@Before(BlogInterceptor.class)
public class BlogController extends Controller {
	
	public void index() {
		setAttr("blogList", Blog.dao.find("select * from blog order by id asc"));
		render("blog.jsp");
	}
	
	public void add() {
	}
	
	@Before(BlogValidator.class)
	public void save() {
		getModel(Blog.class).save();
		redirect("/blog");
	}
	
	public void edit() {
		setAttr("blog", Blog.dao.findById(getParaToInt()));
	}
	
	@Before(BlogValidator.class)
	public void update() {
		getModel(Blog.class).update();
		redirect("/blog");
	}
	
	public void delete() {
		Blog.dao.deleteById(getParaToInt());
		redirect("/blog");
	}
}


