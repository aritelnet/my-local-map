package net.aritel.apps.mylocalmap.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.aritel.apps.mylocalmap.db.updater.Updater;

/**
 * Database
 * 
 * @author Aritel
 */
public class DB {
	
	private static boolean init;
	
	public static Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
		if (!init) {
			new Updater().update(conn);
			init = true;
		}
		return conn;
	}
	
}
