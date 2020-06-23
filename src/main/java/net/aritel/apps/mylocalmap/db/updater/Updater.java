package net.aritel.apps.mylocalmap.db.updater;

import java.sql.Connection;
import java.sql.SQLException;

public class Updater {

	public Void update(Connection con) throws SQLException {
		if (!new Update_Init().check(con)) {
			new Update_Init().update(con);
		}
		if (!new Update_1().check(con)) {
			new Update_1().update(con);
		}
		
		
		
		return null;
	}

}
