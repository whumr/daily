<?php
// +----------------------------------------------------------------------
// | ThinkPHP [ WE CAN DO IT JUST THINK ]
// +----------------------------------------------------------------------
// | Copyright (c) 2006-2014 http://thinkphp.cn All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.apache.org/licenses/LICENSE-2.0 )
// +----------------------------------------------------------------------
// | Author: liu21st <liu21st@gmail.com>
// +----------------------------------------------------------------------

// 应用入口文件

define('BIND_MODULE', 'Home'); 
// 绑定Home模块到当前入口文件
define('BIND_CONTROLLER','Index'); 
// 绑定Index控制器到当前入口文件
define('APP_PATH','./Application/');
require './ThinkPHP/ThinkPHP.php';