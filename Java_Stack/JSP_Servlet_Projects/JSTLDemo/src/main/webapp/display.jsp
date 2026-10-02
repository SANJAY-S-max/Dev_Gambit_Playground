<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
  	<%-- <%
  		String name = request.getAttribute("label").toString();
  		out.print(name); 
  	%> --%>
  	<%-- ${student.name}<br> --%>
  	<%-- ${student.rollno }<br> --%>
  	<%-- ${students}<br> --%>
  	
  	<%-- <c:out value="Hello World"/><br>
  	<c:forEach items="${students}" var ="s">
  		${s}<br>
  	</c:forEach> --%>
  	
  	<sql:setDataSource var="db" driver="com.mysql.cj.jdbc.Driver" url="jdbc:mysql://localhost:3306/demojsp" user ="root" password="Thilagasanjay"/>
  	<sql:query var="rs" dataSource="${db}">select * from users</sql:query>
  	<c:forEach items="${rs.rows}" var="user">
  		<c:out value="${user.id}"></c:out>
  		<c:out value="${user.name}"></c:out>
  		<c:out value="${user.email}"></c:out><br>
  	</c:forEach>
</body>
</html>