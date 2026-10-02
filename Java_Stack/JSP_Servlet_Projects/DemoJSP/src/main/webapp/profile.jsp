<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.demo.DBConnection" %>

<!DOCTYPE html>
<html>
<head>
    <title>User List</title>
</head>

<body>

    <h1>User List</h1>

    <%
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            // Step 1: Get database connection
            connection = DBConnection.getConnection();

            // Step 2: Create Statement
            statement = connection.createStatement();

            // Step 3: Execute SQL query
            resultSet = statement.executeQuery(
                "SELECT id, name, email FROM users"
            );

            // Step 4: Read the result
            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
    %>

                <p>
                    ID: <%= id %>
                    |
                    Name: <%= name %>
                    |
                    Email: <%= email %>
                </p>

    <%
            }

        } catch (Exception e) {

            out.println("<h2>Database Error</h2>");
            out.println("<p>" + e.getMessage() + "</p>");

        } finally {

            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
    %>

</body>
</html>
