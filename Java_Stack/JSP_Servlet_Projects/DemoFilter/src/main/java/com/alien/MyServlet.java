package com.alien;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet{
	protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		int aid = Integer.parseInt(req.getParameter("aid"));
		String aname = req.getParameter("aname");
		out.println("Welcome"+ aname + aid);
	}
}
