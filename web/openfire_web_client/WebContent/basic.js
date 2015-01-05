var BOSH_SERVICE = 'http://mr.cn:7070/http-bind/';
var GROUP_SERVICE = 'group.mr.cn';
var connection = null;
var fromId = null;
var chat_index = 1;
var tab_map = new Map();

function clearLog() {
	$('#log_div').empty();
}

function sendTest() {
	
}

function addChatTab(title, jid, isGroup, message) {
	var chat_id;
	//已存在，focus
	if (tab_map.containsKey(jid)) {
		chat_id = tab_map.get(jid);
		$('#' + chat_id).show();
		$('#chat_div .sub_div').each(function() {
			$(this).removeClass('highlight');
		});
		$('#' + chat_id).addClass('highlight');
	} else {
		$('#chat_div .sub_div').each(function() {
			$(this).removeClass('highlight');
		});
		
		//放入map
		chat_id = 'chat-' + chat_index++;
		tab_map.put(jid, chat_id);
		
		//添加聊天窗口
		$('#chat_div').append('<div class="sub_div" id="' + chat_id + '">'
			+ '<div class="title_div">' + title + '<label class="titile_label">关闭</label></div>'
			+ '<div class="msg_div"></div>'
			+ '<input type="text" class="send_text"><input type="button" value="发送" />'
			+ '</div>');
	
		//发送消息
		$('#' + chat_id + ' input[type="button"]').bind('click', function() {
			var msg = $('#' + chat_id + ' input[type="text"]').val();
			var type = isGroup ? 'groupchat' : 'chat';
			connection.send($msg({to:jid,from:fromId,type:type})
					.cnode(Strophe.xmlElement('body', '', msg)));
			var msg_div = $('#' + chat_id + ' .msg_div');
			msg_div.append('<div>' + new Date().toLocaleTimeString() + "  Me:  " + msg + '</div>');
			msg_div.attr("scrollTop", msg_div.attr("scrollHeight"));
			$('#' + chat_id + ' input[type="text"]').val('');
		});

		//高亮
		$('#' + chat_id).addClass('highlight');
		//关闭
		$('#' + chat_id + ' label').bind('click', function() {
			$('#' + chat_id).hide();
		});
	}
	
	if (message) {
		var msg_div = $('#' + chat_id + ' .msg_div');
		msg_div.append('<div>' + message + '</div>');
		msg_div.attr("scrollTop", msg_div.attr("scrollHeight"));
	}
}

$(document).ready(function() {
	$("#main_frame").accordion({heightStyle:"content"});
	
	Strophe.addNamespace('MUC_ADMIN', Strophe.NS.MUC + "#admin");
	Strophe.addNamespace('JOIN_GROUP', 'jabber:iq:register');
	connection = new Strophe.Connection(BOSH_SERVICE);
	connection.rawInput = function(data) {log(data);};
//	connection.rawOutput = rawOutput;

	$('#connect').bind('click', function() {
		var button = $('#connect').get(0);
		if (button.value == 'connect') {
			button.value = 'disconnect';

			fromId = $('#jid').val();

			connection.connect($('#jid').get(0).value, 
				$('#pass').get(0).value, onConnect);
		} else {
			button.value = 'connect';
			connection.disconnect();
		}
	});

	$('#send').bind('click', function() {
		var paser = new DOMParser();
		var doc = paser.parseFromString($('#msg').val(), "text/xml").childNodes[0];
		connection.send(doc);
	});

	$('#msg').keypress(function(e) {
		if (e.which == 13) {
			$('#send').click();
		}
	});
	
	//新建群
	$('#create_group').bind('click', function() {
		var name = prompt('输入群名称:');
		if (name != null && name != '')
			createGroup(name);
	});

	//加入群
	$('#join_group').bind('click', function() {
		var to_group = $('#chat_jid').val();
		var reason = prompt('输入申请理由:');
		if (reason != null && to_group != '')
			joinGroup(to_group, reason);
	});
});

function createGroup(name) {
	connection.send($pres({from:fromId,to:name+'@'+GROUP_SERVICE})
			.c('x', {xmlns : Strophe.NS.MUC}).tree());
	getGroups();
}

function joinGroup(group, reason) {
//	<iq from='mr2@mr.cn' to='7@group.mr.cn' type='get'><query xmlns='jabber:iq:register'><reason>我是消息</reason></query></iq>
	connection.sendIQ($iq({from:fromId,to:group,type:'get'})
		.c('query',{xmlns:Strophe.NS.JOIN_GROUP})
		.cnode(Strophe.xmlElement('reason', '', reason)),
		function(result) {
			//反馈结果
//			alert('result:' + Strophe.serialize(result));
		}, onError());
}

function log(msg) {$('#log_div').append('<div></div>').append(document.createTextNode(msg));}

function onConnect(status) {
	if (status == Strophe.Status.CONNECTING) {
		log('Strophe is connecting.');
	} else if (status == Strophe.Status.CONNFAIL) {
		log('Strophe failed to connect.');
		$('#connect').get(0).value = 'connect';
	} else if (status == Strophe.Status.DISCONNECTING) {
		log('Strophe is disconnecting.');
	} else if (status == Strophe.Status.DISCONNECTED) {
		log('Strophe is disconnected.');
		$('#connect').get(0).value = 'connect';
	} else if (status == Strophe.Status.CONNECTED) {
		log('Strophe is connected.');

		connection.addHandler(onMessage, null, 'message', null, null, null);
		connection.addHandler(onIq, null, 'iq', null, null, null);
		connection.addHandler(onPres, null, 'pres', null, null, null);
		
		connection.send($pres().tree());
		
		getRoster();
		getGroups();
	}
}

function onMessage(msg) {
	var from = msg.getAttribute('from');
	var type = msg.getAttribute('type');
	var bodys = msg.getElementsByTagName('body');
	var groups = msg.getElementsByTagName('group');
	var msg = null, group_id = null;
	//消息内容
	if (bodys.length > 0)
		msg = new Date().toLocaleTimeString() + '  ' + from + ': ' + Strophe.getText(bodys[0]);
	//组id
	if (groups.length > 0 && type == 'groupchat') 
		group_id = groups[0].getAttribute('id');
	var jid = from.split('/')[0];
	if (group_id)
		jid = group_id + '@group.mr.cn';
	addChatTab(jid, jid, group_id, msg);
	//alert('jid=' + jid + '\ngroup_id=' + group_id + '\nmsg=' + msg);
	return true;
}

function onIq(iq) {
	appendTolog(Strophe.serialize(iq));
//	<iq xmlns='jabber:client' type='result' id='111-5' from='10@group.mr.cn' to='mr1@mr.cn/50e53a56'>
//	<query xmlns='jabber:iq:register' applier='mr@mr.cn/4'><![CDATA[用户[mr]申请加入群[null].申请理由：我是习近平]]></query></iq>
	var query = iq.firstChild;
	if (query && query.tagName == 'query'
		&& query.getAttribute('xmlns') == Strophe.NS.JOIN_GROUP
		&& query.getAttribute('applier')) {
		var result = query.getAttribute('result');
		if (result) {
			if (result == '1')
				layer.msg('申请通过');
			else if (result == '2')
				layer.msg('申请拒绝');
		} else {
			var applier = query.getAttribute('applier');
			var content = query.textContent;
			var confirm = null;
			confirm = $.layer({
				shade : [0.5], //不显示遮罩
				area : ['200px','auto'],
				dialog : {msg:content,btns:2,type:4,btn:['同意','拒绝'],
					yes : function(){layer.close(confirm);processJoinApply(applier, true);},
					no : function(){layer.close(confirm);processJoinApply(applier, false);}
				}
			});
		}
	}
	return true;
}

function processJoinApply(applier, result) {
//	<iq type="set" to="group.mr.cn"><query xmlns="jabber:iq:register" applier="mr2@mr.cn/7" result="1"/></iq>
	var accept = result ? '1' : '2';
	var iq = $iq({from:fromId,to:GROUP_SERVICE,type:'set'})
	.c('query',{xmlns:Strophe.NS.JOIN_GROUP,applier:applier,result:accept});
	connection.sendIQ(iq, function(result){
	}, onError());
}

function onPres(pres) {appendTolog(Strophe.serialize(pres));return true;}

//取好友
function getRoster() {
	var iq = $iq({from:fromId,type:'get'}).c('query',{xmlns:Strophe.NS.ROSTER});
	connection.sendIQ(iq, function(result){
		var friends = new Array();
		Strophe.forEachChild(result.firstChild, 'item', function (elem) {
            var jid = elem.getAttribute('jid');
            var name = elem.getAttribute('name');
            var group = '';
            var groups = elem.childNodes;
            if (groups != null && groups.length >0) {
            	for ( var i = 0; i < groups.length; i++) {
					if (groups[i].tagName.toLowerCase() == 'group') {
						group = groups[i].textContent;
						break;
					}
				}
            }
            friends.push({jid:jid,name:name,group:group});
        });
		appendFriends(friends);
	}, onError());
}

function appendFriends(friends) {
	var map = new Map();
	for (var i = 0; i < friends.length; i++) {
		var group = friends[i].group;
		if (map.containsKey(group)) {
			map.get(group).push(friends[i]);
		} else {
			var new_group = new Array(friends[i]);
			map.put(group, new_group);
		}
	}
	$('#friends_list').empty();
	var groups = map.keys();
	for (var i = 0; i < groups.length; i++) {
		var group = groups[i];
		var gruop_p = $('<p class="friend_group_li">' + group + '</p>');
		var gruop_div = $('<div></div>');
		var friends = map.get(group);
		var group_ul = $('<ul></ul>');
		for (var j = 0; j < friends.length; j++) {
			var friend = friends[j];
			group_ul.append('<li class="friend_li"><input type="hidden" value="' 
					+ friend.jid + '"/>' + friend.name + '</li>');
		}
		gruop_div.append(group_ul);
		$('#friends_list').append(gruop_p);
		$('#friends_list').append(gruop_div);
	}
	$('#friends_list .friend_li').bind('click', function() {
		var jid = $(this).children()[0].value;
		appendMembers([{jid:jid,nick:$(this).text()}]);
		$('#chat_jid').val(jid);
		//添加聊天tab
		addChatTab(jid.split('@')[0], jid);
	});

	$("#friends_list").accordion({heightStyle:"content"});
}

function getGroups() {
	var iq = $iq({from:fromId,to:GROUP_SERVICE,type:'get'}).c('query',{xmlns:Strophe.NS.DISCO_ITEMS});
	connection.sendIQ(iq, function(result){
		var groups = new Array();
		Strophe.forEachChild(result.firstChild, 'item', function (elem) {
            var jid = elem.getAttribute('jid');
            var name = elem.getAttribute('name');
            groups.push({jid:jid,name:name});
        });
		appendGroups(groups);
	}, onError());
}

function appendGroups(groups) {
	$('#groups_list').empty();
	for (var i = 0; i < groups.length; i++) {
		var group = groups[i];
		$('#groups_list').append('<li class="group_li"><input type="hidden" value="' 
				+ group.jid + '"/>' + group.name + '</li>');
	}
	
	$('#groups_list li').bind('click', function() {
		var jid = $(this).children()[0].value;
		$('#chat_jid').val(jid);
		getGroupMembers(jid.split('@')[0]);
		//添加聊天tab
		addChatTab($(this).text(), jid, true);
	});
}

function appendMembers(members) {
	$('#member_list').empty();
	for (var i = 0; i < members.length; i++) {
		var member = members[i];
		$('#member_list').append('<li class="friend_li"><input type="hidden" value="'
				+ member.jid + '"/>' + member.nick + '</li>');
	}
}

function onError(error) {
	if (error != null)
		alert('error' + Strophe.serialize(error));
}

function getGroupMembers(group_id) {
	var iq = $iq({from:fromId,to:group_id+'@'+GROUP_SERVICE,type:'get'})
		.c('query',{xmlns:Strophe.NS.MUC_ADMIN})
		.c('item',{affiliation:'member'});
	connection.sendIQ(iq, function(result){
		var members = new Array();
		Strophe.forEachChild(result.firstChild, 'item', function (elem) {
            var jid = elem.getAttribute('jid');
            var nick = elem.getAttribute('nick');
            var role = elem.getAttribute('role');
            members.push({jid:jid,nick:nick,role:role});
        });
		appendMembers(members);
	}, onError());
}

String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
	if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
		return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi" : "g")), replaceWith);
	} else {
		return this.replace(reallyDo, replaceWith);
	}
};

function Map() {
    this.elements = new Array();

    //获取MAP元素个数
    this.size = function() {
        return this.elements.length;
    };

    //判断MAP是否为空
    this.isEmpty = function() {
        return (this.elements.length < 1);
    };

    //删除MAP所有元素
    this.clear = function() {
    	delete elements;
        this.elements = new Array();
    };

    //向MAP中增加元素（key, value) 
    this.put = function(_key, _value) {
        this.elements.push( {
            key : _key,
            value : _value
        });
    };

    //删除指定KEY的元素，成功返回True，失败返回False
    this.removeByKey = function(_key) {
        try {
            for (var i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
        }
        return false;
    };
    
    //获取指定KEY的元素值VALUE，失败返回NULL
    this.get = function(_key) {
        try {
            for (var i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    return this.elements[i].value;
                }
            }
        } catch (e) {
        }
        return undefined;
    };

    //获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
    this.element = function(_index) {
        if (_index < 0 || _index >= this.elements.length) {
            return undefined;
        }
        return this.elements[_index];
    };

    //判断MAP中是否含有指定KEY的元素
    this.containsKey = function(_key) {
        try {
            for (var i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) 
                    return true;
            }
        } catch (e) {
        }
        return false;
    };

    //获取MAP中所有KEY的数组（ARRAY）
    this.keys = function() {
        var arr = new Array();
        for (var i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].key);
        }
        return arr;
    };
}