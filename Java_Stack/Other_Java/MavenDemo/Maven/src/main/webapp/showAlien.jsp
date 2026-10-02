<%@page import="com.sanjay.web.model.Alien"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Alien Details</title>
</head>
<body bgcolor="cyan">
	<%
		// Read alien from session (not request)
		Alien a1 = (Alien) session.getAttribute("alien");
		if (a1 != null) {
			out.println("Alien ID   : " + a1.getAid() + "<br>");
			out.println("Alien Name : " + a1.getAname() + "<br>");
			out.println("Technology : " + a1.getTech() + "<br>");
		} else {
			out.println("No Alien found with that ID.");
		}
	%>
</body>
</html>