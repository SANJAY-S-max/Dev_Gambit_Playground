<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%
		response.setHeader("Cache-Control", "no-cache , no-store , must-revalidate");
		if(session.getAttribute("uname")==null){
			response.sendRedirect("login.jsp");
		}
	%>
	Welcome ${uname}
	<a href="videos.jsp">Video</a>
	<form action="${pageContext.request.contextPath}/Logout">
		<input type="submit" value="Logout">
	</form>
</body>
</html>