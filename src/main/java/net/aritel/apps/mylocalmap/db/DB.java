package net.aritel.apps.mylocalmap.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database
 * 
 * @author Aritel
 */
public class DB {
	
	public static Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:h2:~/test");
		return conn;
	}
	
}
