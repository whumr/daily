<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xml:lang="zh-CN" xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="war3,dota,炉石传说,视频,ted,sky,820,鼠大王,小y" />
<meta name="description" content="war3,dota,炉石传说,视频,ted,sky,820,鼠大王,小y" />
<link href="/css/manage.css" media="screen" rel="stylesheet" type="text/css" />
<link href="/css/jqpage.css" media="screen" rel="stylesheet" type="text/css" />
<script src="/js/jquery-1.7.2.min.js" type="text/javascript"></script>
<script src="/js/jqpage.min.js" type="text/javascript"></script>
<style type="text/css">
	.div_left {float: left; margin-right: 50px;}
	.div_right {float: right;}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$('.pagination').jqPagination({
			current_page: '${videos.pageNumber}',
			max_page	: '${videos.totalPage}',
			page_string : '当前第${videos.pageNumber}页,共${videos.totalPage}页',
			paged		: function(page) {
				document.location.href = '/' + page;
			}
		});
	});
</script>
</head>
<body>
	<div class="manage_container">
		<div class="manage_head">
			<div class="manage_logo">
				war3 dota 炉石传说视频
			</div>
			<div id="nav">
				<ul>
					<li><a href="/"><b>首页</b></a></li>
					<li><a href="/admin"><b>视频管理</b></a></li>
				</ul>
			</div>
		</div>
		<div class="main">
			<h1>
				videos
			</h1>
			<div class="table_box">
				<c:forEach items="${videos.list}" var="video" varStatus="status">
					<div <c:if test="${status.index%2==0}">class="div_left"</c:if>
						<c:if test="${status.index%2==1}">class="div_right"</c:if>>
						<div id="player${video.id}" style="width:360px;height:300px">
						</div>
						<table style="width: 400px; margin-bottom: 20px;">
							<tr><td>${video.title}</td></tr>
							<tr><td>${video.user_name}--<fmt:formatDate value="${video.published}" pattern="yyyy-MM-dd HH:mm:ss" /></td></tr>
							<tr><td>播放：${video.view_count};顶：${video.up_count}踩：${video.down_count}</td></tr>
						</table>
					</div>
				</c:forEach>
			</div>
			<script type="text/javascript" src="http://player.youku.com/jsapi">
				function showVideo(domid, vid) {
					player = new YKU.Player(domid, {client_id : 'f4068d8c2d21d6cd', vid : vid, show_related : false});
				}
				<c:forEach items="${videos.list}" var="video">
					showVideo('player${video.id}', '${video.y_id}');
				</c:forEach>
			</script>

			<div class="pagination">
				<a href="#" class="first" data-action="first">&laquo;</a>
				<a href="#" class="previous" data-action="previous">&lsaquo;</a>
				<input type="text" readonly="readonly" data-max-page="${videos.totalPage}" />
				<a href="#" class="next" data-action="next">&rsaquo;</a>
				<a href="#" class="last" data-action="last">&raquo;</a>
			</div>
		</div>
	</div>
</body>
</html>