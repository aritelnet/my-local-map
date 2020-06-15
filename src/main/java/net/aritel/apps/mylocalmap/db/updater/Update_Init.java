package net.aritel.apps.mylocalmap.db.updater;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Update_Init {
	public boolean check(Connection con) {
		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM app_version")) {
			return ps.executeQuery().next();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void update(Connection con) throws SQLException {
		try (Statement st = con.createStatement()) {
			st.executeUpdate("CREATE TABLE app_version (id int auto_increment, version int)");
			st.executeUpdate("INSERT INTO app_version values(null, 1)");
			st.executeUpdate("CREATE TABLE map_units (id long auto_increment, name varchar, image BLOB)");
		}
	}
}
