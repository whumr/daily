<?php
namespace Home\Model;
use Think\Model;
class BlogModel extends Model {
	protected $trueTableName = 'blog';
	
	protected $fields = array('id', 'title', 'content','_pk'=>'id',        
		'_type'=>array('id'=>'int','title'=>'varchar','content'=>'mediumtext')    
	);
	
	protected $_validate = array(     
		array('id','number','id不能为空！',1,'',2),     
		array('title','2,100','标题长度2-100！',1,'length'),     
		array('content','require','内容不能为空！')
	);
}
