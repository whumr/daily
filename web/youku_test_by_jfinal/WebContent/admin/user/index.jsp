<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% String basePath = "/admin/user/"; %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xml:lang="zh-CN" xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link href="/css/manage.css" media="screen" rel="stylesheet" type="text/css" />
<link href="/css/jqpage.css" media="screen" rel="stylesheet" type="text/css" />
<script src="/js/jquery-1.7.2.min.js" type="text/javascript"></script>
<script src="/js/jqpage.min.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.pagination').jqPagination({
			current_page: '${data.pageNumber}',
			max_page	: '${data.totalPage}',
			page_string : '当前第${data.pageNumber}页,共${data.totalPage}页',
			paged		: function(page) {
				document.location.href = '<%=basePath%>' + page;
			}
		});
	});
	
	function showAdd() {
		var name = prompt('输入用户名:');
		if (name != null && name != '') {
			$.ajax({url:'<%=basePath%>add',type:'POST',
				data:'name=' + name,cache:false,
				success:function(data) {
					if (data.error)
						alert(data.error);	
					else
						document.location.href = '<%=basePath%>' + '${data.pageNumber}';
				}
			});
		}
	}
	
	function deleteUser(id) {
		if (confirm('确认删除？')) {
			$.ajax({url:'<%=basePath%>deleteUser',type:'POST',
				data:'id=' + id,cache:false,
				success:function(data) {
					if (data.error) alert(data.error);	
					else document.location.href = '<%=basePath%>' + '${data.pageNumber}';
				}
			});
		}
	}

	function refreshUser(id) {
		$.ajax({url:'<%=basePath%>refreshUser',type:'POST',
			data:'id=' + id,cache:false,
			success:function(data) {
				if (data.error) 
					alert(data.error);	
				else {
					alert('数据更新中....');					
					document.location.href = '<%=basePath%>' + '${data.pageNumber}';
				}
			}
		});
	}

	function reloadUser(id) {
		if (confirm('确认重新加载所有数据？')) {
			$.ajax({url:'<%=basePath%>reloadUser',type:'POST',
				data:'id=' + id,cache:false,
				success:function(data) {
					if (data.error) alert(data.error);	
					else {
						alert('数据更新中....');					
						document.location.href = '<%=basePath%>' + '${data.pageNumber}';
					}
				}
			});
		}
	}
</script>
</head>
<body>
	<div class="manage_container">
		<div class="manage_head">
			<div class="manage_logo">后台管理系统</div>
			<div id="nav">
				<ul>
					<li><a href="/"><b>首页</b></a></li>
					<li><a href="/admin/user"><b>用户管理</b></a></li>
					<%--
					<li><a href="/admin/vedio"><b>视频管理</b></a></li>
					 --%>
				</ul>
			</div>
		</div>
		<div class="main">
			<h1>
				用户管理&nbsp;&nbsp; <a href="#" onclick="showAdd()">新增用户</a>
			</h1>
			<div class="table_box">
				<table class="list">
					<tr>
						<th style="display: none"/>
						<th style="display: none"/>
						<th width="12%">用户名</th>
						<th width="13%">注册时间</th>
						<th width="5%">视频个数</th>
						<th width="5%">专辑个数</th>
						<th width="15%">优酷空间</th>
						<th>个人说明</th>
						<th width="5%">操作</th>
					</tr>
					<c:forEach items="${data.list}" var="user">
						<tr>
							<td style="display: none"><input type="hidden" value="${user.id}"/></td>
							<td style="display: none"><input type="hidden" value="${user.y_id}"/></td>
							<td>${user.name}<br/><img src="${user.avatar}" alt="${user.name}" /></td>
							<td><fmt:formatDate value="${user.regist_time}" pattern="yyyy-MM-dd" /></td>
							<td>${user.videos_count}</td>
							<td>${user.playlists_count}</td>
							<td><a href="${user.link}" target="_blank">${user.link}</a></td>
							<td style="word-break:break-all;">${user.description}</td>
							<td>
								<a href="#" onclick="deleteUser(${user.id})">删除</a><br/><br/>
								<a href="#" onclick="refreshUser(${user.id})">刷新</a><br/><br/>
								<a href="#" onclick="reloadUser(${user.id})">重载</a>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<div class="pagination">
				<a href="#" class="first" data-action="first">&laquo;</a>
				<a href="#" class="previous" data-action="previous">&lsaquo;</a>
				<input type="text" readonly="readonly" data-max-page="${data.totalPage}" />
				<a href="#" class="next" data-action="next">&rsaquo;</a>
				<a href="#" class="last" data-action="last">&raquo;</a>
			</div>
		</div>
	</div>
</body>
</html>