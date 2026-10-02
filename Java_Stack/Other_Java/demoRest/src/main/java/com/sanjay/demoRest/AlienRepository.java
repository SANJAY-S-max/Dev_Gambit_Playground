package com.sanjay.demoRest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlienRepository {

	// Force the MySQL driver to register itself with DriverManager
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError("MySQL JDBC Driver not found: " + e.getMessage());
		}
	}

	private final String url = "jdbc:mysql://localhost:3306/alien_db?sslMode=DISABLED&serverTimezone=UTC";

	private final String username = "root";
	private final String password = "Thilagasanjay";

	public List<Alien> getAliens() throws SQLException {

		List<Alien> aliens = new ArrayList<>();

		String sql = "SELECT id, name, points FROM alien";

		try (Connection con = DriverManager.getConnection(url, username, password);
				PreparedStatement st = con.prepareStatement(sql);
				ResultSet rs = st.executeQuery()) {

			while (rs.next()) {

				Alien alien = new Alien();

				alien.setId(rs.getInt("id"));
				alien.setName(rs.getString("name"));
				alien.setPoints(rs.getInt("points"));

				aliens.add(alien);
			}
		}

		return aliens;
	}

	public Alien getAlien(int id) throws SQLException {

		String sql = "SELECT id, name, points FROM alien WHERE id = ?";

		try (Connection con = DriverManager.getConnection(url, username, password);
				PreparedStatement st = con.prepareStatement(sql)) {

			st.setInt(1, id);

			try (ResultSet rs = st.executeQuery()) {

				if (rs.next()) {

					Alien alien = new Alien();

					alien.setId(rs.getInt("id"));
					alien.setName(rs.getString("name"));
					alien.setPoints(rs.getInt("points"));

					return alien;
				}
			}
		}

		return null;
	}

	public void create(Alien alien) throws SQLException {

		String sql = "INSERT INTO alien (id, name, points) VALUES (?, ?, ?)";

		try (Connection con = DriverManager.getConnection(url, username, password);
				PreparedStatement st = con.prepareStatement(sql)) {

			st.setInt(1, alien.getId());
			st.setString(2, alien.getName());
			st.setInt(3, alien.getPoints());

			st.executeUpdate();
		}
	}

	public void update(Alien alien) throws SQLException {

		String sql = "UPDATE alien set name=?, points=? where id =?";

		try (Connection con = DriverManager.getConnection(url, username, password);
				PreparedStatement st = con.prepareStatement(sql)) {

			st.setString(1, alien.getName());
			st.setInt(2, alien.getPoints());
			st.setInt(3, alien.getId());

			st.executeUpdate();
		}
	}

	public void delete(int id) throws SQLException {
		String sql = "DELETE FROM alien WHERE id=?";
		try (Connection con = DriverManager.getConnection(url, username, password);
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, id);
			st.executeUpdate();
		}
	}
}