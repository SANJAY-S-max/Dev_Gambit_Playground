package com.sanjay;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@WebServlet("/DemoServlet")
public class DemoServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		PrintWriter out = res.getWriter();
		List<Student> studs = Arrays.asList(new Student(1,"Navin"),new Student(2,"Aathi"),new Student(3,"Sanjay"));

//		out.println("Hello World");
//		Student s = new Student(1,"Sanjay");
		
//		String name = "Sanjay";
		 req.setAttribute("students",studs);
		RequestDispatcher rd = req.getRequestDispatcher("/display.jsp");
		rd.forward(req,res);
	}
}
