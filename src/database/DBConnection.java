package database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.User;

public class DBConnection {
	
	public static Connection getConnectionToDatabase() {
		Connection connection = null;

		try {
			InitialContext context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup("java:/comp/env/jdbc/webassignment");
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		} catch (NamingException e) {
		    System.out.println("Connection Failed! Check output console (NamingException)");
		    e.printStackTrace();
		}

		if (connection != null) {
			System.out.println("Connection made to DB!");
		}
		return connection;
	}
}
