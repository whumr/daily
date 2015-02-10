<?php
namespace Home\Model;
use Think\Model;
class ShopModel extends Model {
	protected $trueTableName = 'shop';
	
	protected $fields = array('id','user_id','name','tags','description',
		'address','logo_url','telephone','open_time','create_time','_pk'=>'id',      
		  
		'_type'=>array('id'=>'int','user_id'=>'int','name'=>'varchar','tags'=>'varchar','description'=>'varchar',
			'address'=>'varchar','logo_url'=>'varchar','telephone'=>'varchar','open_time'=>'varchar','create_time'=>'timestamp')    
	);
}
