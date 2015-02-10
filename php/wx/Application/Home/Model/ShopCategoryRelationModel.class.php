<?php
namespace Home\Model;
use Think\Model;
class ShopCategoryModel extends Model {
	protected $trueTableName = 'shop_category_relation';
	
	protected $fields = array('id', 'shop_id', 'category_id','_pk'=>'id',        
		'_type'=>array('id'=>'int','shop_id'=>'int','category_id'=>'int')    
	);
}
