<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>login</title>
</head>
<body>
	<form action="/user" method="post">
		<table>
			<tr>
				<td>user:</td>
				<td><input name="user" type="text"></td>
			</tr>
			<tr>
				<td>password:</td>
				<td><input name="password" type="password"></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="login"></td>
			</tr>
		</table>
	</form>
</body>
</html>