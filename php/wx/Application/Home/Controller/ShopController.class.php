<?php
namespace Home\Controller;
class ShopController extends AdminController {
	
	public function _initialize(){
		parent::_initialize();
		if (!parent::checkAdminSession())
			exit();
	}
	
	public function index(){
		$this->display();
    }
	
	public function edit(){
		$shop = session('shop');
		if (!empty($shop)) 
			foreach($shop as $key=>$value)
				\Think\Log::record($key.'=>'.$value,'WARN');		
			$this->assign('shop', $shop);		
		$this->display();
    }
	
	public function save() {
		$id = $_POST['id'];
		$name = $_POST['name'];
		$tags = $_POST['tags'];
		$description = $_POST['description'];
		$data['status'] = 1;
		if (empty($name) || empty($tags)) {
			$data['status'] = 0;
			$data['error'] = '名称和标签不能为空!';
		} else {
			$user = session('user');
			$shop = D('Shop'); 
			$shop->user_id = $user['id'];
			$shop->name = $name;
			$shop->tags = $tags;
			$shop->description = $description;
			try {
				if (empty($id))
					$shop->add();
				else
					$shop->where('id='.$id)->save();
				//更新session中的shop
				$session_shop = session('shop');
				$session_shop['name'] = $name;
				$session_shop['tags'] = $tags;
				$session_shop['description'] = $description;
				session('shop',$session_shop);
			} catch (\Think\Exception $e) {
				\Think\Log::record('save shop error : '.$e['message'],'ERROR');
				$data['status'] = 0;
				$data['error'] = '保存失败！';
			}
		}
		$this->ajaxReturn($data);
	}
}