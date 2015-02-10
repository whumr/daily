<?php
namespace Home\Model;
use Think\Model;
class MenuCategoryModel extends Model {
	protected $trueTableName = 'menu_category';
	
	protected $fields = array('id','shop_id','name','parent_id','_pk'=>'id',        
		'_type'=>array('id'=>'int','shop_id'=>'int','name'=>'varchar','parent_id'=>'int')    
	);
}
