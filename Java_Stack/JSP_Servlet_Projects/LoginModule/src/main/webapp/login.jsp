<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/Login" method="post">
		Enter UserName:<input type="text" name="uname"><br>
		Enter Password:<input type="password" name="pass"><br>
		<input type="submit" value="login">
	</form>
</body>
</html>