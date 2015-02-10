<?php
namespace Home\Model;
use Think\Model;
class UserModel extends Model {
	protected $trueTableName = 'user';
	
	protected $fields = array('id', 'name', 'password','type','_pk'=>'id',        
		'_type'=>array('id'=>'int','title'=>'varchar','password'=>'varchar','type'=>'char')    
	);
}
