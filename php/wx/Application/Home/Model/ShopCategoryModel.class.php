<?php
namespace Home\Model;
use Think\Model;
class ShopCategoryModel extends Model {
	protected $trueTableName = 'shop_category';
	
	protected $fields = array('id', 'name', 'parent_id','type','_pk'=>'id',        
		'_type'=>array('id'=>'int','name'=>'varchar','parent_id'=>'int','type'=>'enum')    
	);
}
