<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>index</title>
</head>
<body>

	<% if (request.getSession().getAttribute("user") == null) {%>
		<script type="text/javascript">
			window.location.href="https://localhost:8443/login.jsp"; 
		</script>
	<% }else {
		out.println(request.getSession().getAttribute("user").toString());
	} %>
</body>
</html>