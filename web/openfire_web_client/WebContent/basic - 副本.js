var BOSH_SERVICE = 'http://mr.cn:7070/http-bind/';
var GROUP_SERVICE = 'group.mr.cn';
var connection = null;
var toId = null;
var fromId = null;
var chat_index = 1;
var tab_map = new Map();

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
		addChatTab(jid, jid, false);
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
		var jid = $(this).children()[0].value 
			+ '(' + $(this).text() + ')';
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

function addChatTab(title, jid, isGroup) {
	//已存在，focus
	if (tab_map.containsKey(jid)) {
		var index = tab_map.get(jid).split('-')[1];
		$(".group_his").tabs("option", "active", index);
	} else {
		var div_id = 'tabs-' + chat_index++;
		tab_map.put(jid, div_id);
		$('#group_his_title').append('<li><a href="#' + div_id + '">' + title + '</a></li>');
		$('#group_his').append('<div id="' + div_id + '"><div class="his_div"></div>' + 
		'<input type="text"><input type="button" value="发送" /></div>');
		$('#' + div_id + ' input[type="button"]').bind('click', function() {
			var msg = $('#' + div_id + ' input[type="text"]').val();
			var type = isGroup ? 'groupchat' : 'chat';
			connection.send($msg({to:toId,from:fromId,type:type})
					.cnode(Strophe.xmlElement('body', '', msg)));
			
			var his_div = $('#' + div_id + ' .his_div');
			his_div.append('<div>' + new Date().toLocaleTimeString() + "  Me:  " + msg + '</div>');
			his_div.attr("scrollTop", his_div.attr("scrollHeight"));
			$('#' + div_id + ' input[type="text"]').val('');
		});
		$("#group_his").tabs('option', 'active', chat_index - 1);
		$("#group_his").tabs('refresh');
	}
}

$(document).ready(function() {
	$("#main_frame").accordion({heightStyle:"content"});
	$("#group_his").tabs({active: 1});
	
	
	Strophe.addNamespace('MUC_ADMIN', Strophe.NS.MUC + "#admin");
	Strophe.addNamespace('JOIN_GROUP', 'jabber:iq:register');
	connection = new Strophe.Connection(BOSH_SERVICE);
//	connection.rawInput = rawInput;
//	connection.rawOutput = rawOutput;

	$('#connect').bind('click', function() {
		var button = $('#connect').get(0);
		if (button.value == 'connect') {
			button.value = 'disconnect';

			fromId = $('#jid').val();
			toId = $('#tojid').val();

			connection.connect($('#jid').get(0).value, 
				$('#pass').get(0).value, onConnect);
		} else {
			button.value = 'connect';
			connection.disconnect();
		}
	});

	$('#send').bind('click', function() {
		msg = $('#msg').val();

		toId = $('#tojid').val();
		var reply = $msg({to : toId,from : fromId,type : 'chat'})
			.cnode(Strophe.xmlElement('body', '', msg));
		connection.send(reply.tree());

		appendToHis(new Date().toLocaleTimeString() + "  Me:  " + msg);
		$('#msg').val('');
	});

	$('#msg').keypress(function(e) {
		if (e.which == 13) {
			$('#send').click();
		}
	});
	
	//取好友
	$('#get_roster').bind('click', function() {
		var iq = $iq({
			from : fromId,
			type : 'get'
		}).c('query', {
			xmlns : Strophe.NS.ROSTER
		});
		connection.send(iq);
	});
	
	//取会议室列表
	$('#get_disco').bind('click', function() {
		connection.send($iq({type : 'get', xmlns : Strophe.NS.CLIENT})
			.c('query', {xmlns : Strophe.NS.DISCO_ITEMS}).tree());
	});
	
	//发送消息
	$('#send_test').bind('click', function() {
		var paser = new DOMParser();
		var doc = paser.parseFromString($('#message').val(), "text/xml").childNodes[0];
		connection.send(doc);
	});

	//发送群消息
	$('#chat_send').bind('click', function() {
		var msg = $('#chat_msg').val(),
			toId = $('#chat_jid').val(),
			reply = $msg({to:toId,from:fromId,type:'groupchat'}).cnode(Strophe.xmlElement('body', '', msg));
		connection.send(reply.tree());
		appendToGroupHis(new Date().toLocaleTimeString() + "  Me:  " + msg);
		$('#chat_msg').val('');
	});

	$('#chat_msg').keypress(function(e) {
		if (e.which == 13) {
			$('#chat_msg').click();
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

function appendToGroupHis(msg) {
	$('#group_his').append('<div>' + msg + '</div>');
	$('#group_his').attr("scrollTop", $('#group_his').attr("scrollHeight"));
}

String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
	if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
		return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi" : "g")), replaceWith);
	} else {
		return this.replace(reallyDo, replaceWith);
	}
};

function rawInput(data) {log('RECV: ' + data);}
function rawOutput(data) {log('SENT: ' + data);}
function log(msg) {$('#his').append('<div></div>').append(document.createTextNode(msg));}

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
	to = msg.getAttribute('from');
	var from = msg.getAttribute('from');
	var type = msg.getAttribute('type');
	var elems = msg.getElementsByTagName('body');
	
	if (elems.length > 0) {
		var body = elems[0];
		if (type == "chat") {
			appendToHis(new Date().toLocaleTimeString() + '  ' + from + ' say: ' + Strophe.getText(body));
		} else if (type == "groupchat") {
			appendToGroupHis(new Date().toLocaleTimeString() + '  ' + from + ' say: ' + Strophe.getText(body));
		}
	}
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

function appendToHis(msg) {
	$('#his').append('<div>' + msg + '</div>');
	$('#his').attr("scrollTop", $('#his').attr("scrollHeight"));
}

function appendTolog(iq) {
	$('#log').empty();
	$('#log').append('<xmp>' + iq + '</xmp>');
}


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