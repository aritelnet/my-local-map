package net.aritel.apps.mylocalmap.db.updater;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Update_1 {
	public boolean check(Connection con) {
		try (PreparedStatement ps = con.prepareStatement("SELECT version FROM app_version")) {
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() && rs.getInt(1) > 1;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	public void update(Connection con) throws SQLException {
		try (Statement st = con.createStatement()) {
			st.executeUpdate("ALTER TABLE map_units ADD uuid uuid");
			st.executeUpdate("CREATE UNIQUE INDEX ix1_map_units ON map_units(uuid)");
			st.executeUpdate("UPDATE app_version SET version = 2");
			List<Long> list = new ArrayList<Long>();
			try (ResultSet rs = st.executeQuery("SELECT id FROM map_units")) {
				while (rs.next()) {
					list.add(rs.getLong(1));
				}
			}
			for (long id : list) {
				try (PreparedStatement ps = con.prepareStatement("UPDATE map_units SET uuid=? WHERE id=?")) {
					ps.setString(1, UUID.randomUUID().toString());
					ps.setLong(2, id);
					ps.executeUpdate();
				}
			}
		}
	}
}
