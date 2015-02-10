<?php
namespace Home\Controller;
use Think\Controller;
class BlogController extends Controller {

	public $page_size;
	
	public function index(){		
		if (!isset($page_size))
			$page_size = C('PAGE_SIZE', null, 20);		
		$blog = D('Blog'); 
		$page = $_GET['p'];
		if (empty($page))
			$page = 1;
		$count = $blog->count();		
		$list = $blog->order('id desc')->limit(($page - 1) * $page_size, $page_size)->select();
		$this->assign('blogs', $list);
		$this->assign('current_page', $page);
		$this->assign('max_page', $count % $page_size == 0 ? $count / $page_size : floor($count / $page_size) + 1);
		$this->display();
    }

	public function do_add(){
		$blog = D('Blog'); 
		if (!$blog->create()){
			$this->assign('blog', array('title'=>$_POST['title'],'content'=>$_POST['content']));
			$this->assign('error', $blog->getError());
			$this->display('add');
		} else {
			$blog->add(); 
			redirect('index');
		}
    }
	
	public function edit(){
		$id = $_GET['id'];
		if (empty($id))
			redirect('index');			
		else {
			$blog = D('Blog'); 			
			$this->assign('blog', $blog->where('id='.$id)->find());
			$this->display();		
		}		
    }
	
	public function do_edit(){
		$blog = D('Blog'); 
		if (!$blog->create()){
			$this->assign('blog', array('id'=>$_POST['id'],'title'=>$_POST['title'],'content'=>$_POST['content']));
			$this->assign('error', $blog->getError());
			$this->display('edit');
		} else {
			$blog->where('id='.$_POST['id'])->save();
			redirect('index');
		}
    }
	
	public function delete(){
		$id = $_GET['id'];
		if (!empty($id)) {
			$blog = D('Blog'); 
			$blog->delete($id); 			
		} 	
		redirect('index');
    }
}