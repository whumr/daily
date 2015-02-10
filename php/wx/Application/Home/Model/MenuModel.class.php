<?php
namespace Home\Model;
use Think\Model;
class MenuModel extends Model {
	protected $trueTableName = 'menu';
	
	protected $fields = array('id','shop_id','name','category_id','tag','description','img_url','price','_pk'=>'id',        
		'_type'=>array('id'=>'int','shop_id'=>'int','name'=>'varchar','category_id'=>'int',
			'tag'=>'varchar','description'=>'varchar','img_url'=>'varchar','price'=>'decimal')    
	);
}
