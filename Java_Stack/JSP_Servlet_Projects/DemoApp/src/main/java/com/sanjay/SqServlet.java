package com.sanjay;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
//@WebServlet("/sq")
public class SqServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
		PrintWriter out = res.getWriter();
		//Session
//		HttpSession session = req.getSession();
//		int k = (int) session.getAttribute("k");
//		k=k*k;
//		out.print(k);
//		out.println("Hello to Sq:"+k);
		
		//Cookie
		int k=0;
		Cookie cookies[] = req.getCookies();
		for(Cookie c : cookies) {
			if(c.getName().equals("k")) {
				k=Integer.parseInt(c.getValue());
			}
		}
//		
		k=k*k;
//		out.print(k);
		out.println("Hello to Sq:"+k);
	}
	
	/**
	 * ServletConfig & servletContext
	 */
//	private static final long serialVersionUID = 1L;
//	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
//			PrintWriter out = res.getWriter();
//			out.println("Hi <br/>");
////			ServletContext ctx = getServletContext();
////			String str = ctx.getInitParameter("Name");
////			ServletConfig cg = getServletConfig();
////			String str = cg.getInitParameter("Name");
////			out.println(str);
//			
//	}
}
