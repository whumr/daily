<?php
namespace Home\Controller;
use Think\Controller;
class AdminController extends Controller {
	
	public $life_time;
	
	public function _initialize(){
		$this->life_time = C('SESSION_LIFE', null, 600);
	}
	
	public function index(){
		if ($this->checkAdminSession()) {
			$user = session('user');
			$shop = session('shop');
			if (empty($shop)) {
				$shop = D('Shop'); 
				$shop = $shop->where('user_id='.$user['id'])->find();
			} 
			$this->assign('shop', $shop);
			session('shop',$shop);
			$this->display();
		}
    }
	
	public function login(){		
		$this->display();
    }
	
	public function do_login() {
		$account = $_POST['account'];
		$password = $_POST['password'];
		if (empty($account) || empty($password))
			$this->display();
		else {
			$user = D('User'); 
			$user = $user->where('name="'.$account.'" and password="'.$password.'" and type="a"')->find();
			if (empty($user)) {
				$this->assign('error', '用户名或密码错误!');
				$this->display('login'); 
			} else {
				session('user',$user);
				session('lasttime',time());
				redirect('index');
			}
		}
	}
	
	public function logout(){
		session(null);		
		redirect("/admin");
    }
	
	public function checkAdminSession() {
		$user = session('user');
		// if (!empty($user))
			// \Think\Log::record($user['name'].';'.$user['type'],'WARN');		
		if (empty($user)) {
			$this->display('/admin/login'); 
		} else if ($user['type'] != 'a') {
			$this->assign('error', '无权限，请使用管理员账号登录!');
			$this->display('/admin/login'); 
		} else {
			$nowtime = time();  
	       	$s_time = session('lasttime');  
			//\Think\Log::record($nowtime.';'.$s_time.';'.$life_time,'WARN');	
	       	if (($nowtime - $s_time) > $this->life_time) {  
	        	session(null);		
				$this->assign('error', '登录超时，请重新登录!');
				$this->display('/admin/login'); 
	        } else {  
	            session('lasttime', $nowtime);  
				return true;
	        }  
		}
		return false;
	}  
}