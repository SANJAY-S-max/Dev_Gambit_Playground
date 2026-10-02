package com.sanjay.web;

import java.io.IOException;

import com.sanjay.web.dao.AlienDao;
import com.sanjay.web.model.Alien;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class GetAlienController
 */
@WebServlet("/getAlien")
public class GetAlienController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int aid = Integer.parseInt(request.getParameter("aid"));
		AlienDao dao = new AlienDao();
		Alien a1 = dao.getAlien(aid);

		// Store alien in session instead of request attribute
		HttpSession session = request.getSession();
		session.setAttribute("alien", a1);

		// Redirect to showAlien.jsp
		response.sendRedirect("showAlien.jsp");
	}

}

