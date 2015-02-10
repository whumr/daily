<?php
namespace Home\Controller;
use Think\Controller;
class IndexController extends Controller {
    public function index(){		
		redirect("blog");
    }
	
	//http://localhost/index.php?c=Index&a=hello&name=xxx
	//http://localhost/index.php/Home/Index/hello/name/xxx
	//public function hello($name='test'){
    //    echo 'hello, '.$name;
    //}
}