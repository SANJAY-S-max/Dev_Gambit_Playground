package com.sanjay.web.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.sanjay.web.model.Alien;

public class AlienDao {
	
	public Alien getAlien(int aid) {
		Alien a = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/alien", "root", "Thilagasanjay");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM alien WHERE aid=" + aid);
			if (rs.next()) {
				a = new Alien();
				a.setAid(rs.getInt("aid"));
				a.setAname(rs.getString("aname"));
				a.setTech(rs.getString("tech"));
			}
			rs.close();
			st.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace(); // Now errors will show in Tomcat console
		}
		return a;
	}
}
