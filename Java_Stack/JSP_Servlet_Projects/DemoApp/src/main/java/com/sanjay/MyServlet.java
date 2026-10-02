package com.sanjay;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/** Hey Subscribers ...
*
* ServletConfig & ServletContext
*
*
*/
//@WebServlet("/home")
public class MyServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		out.print("Hi ");
		
		ServletConfig cg = getServletConfig();
		String str = cg.getInitParameter("Name");
		out.println(str);
	}

}
