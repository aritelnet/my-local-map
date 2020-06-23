package net.aritel.apps.mylocalmap.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapUnit {
	private long id;
	private String name;
	private byte[] image;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public int insert(Connection con) throws SQLException {
		StringBuilder sql = new StringBuilder("INSERT INTO map_units (uuid, name, image) VALUES (?, ?, ?)");
		try (PreparedStatement ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, UUID.randomUUID().toString());
			ps.setString(2, getName());
			ps.setBytes(3, getImage());
			int i = ps.executeUpdate();
			if (i > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs != null && rs.next()) {
						setId(rs.getLong(1));
					}
				}
			}
			return i;
		}
	}
	
	public static MapUnit find(Connection con, long id) throws SQLException {
		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM map_units WHERE id=?")) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					MapUnit o = new MapUnit();
					o.setId(rs.getLong("id"));
					o.setName(rs.getString("name"));
					o.setImage(rs.getBytes("image"));
					return o;
				}
			}
		}
		return null;
	}
	
	public static List<MapUnit> findAll(Connection con, boolean image) throws SQLException {
		List<MapUnit> list = new ArrayList<MapUnit>();
		try (PreparedStatement ps = con.prepareStatement("SELECT * FROM map_units")) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					MapUnit o = new MapUnit();
					o.setId(rs.getLong("id"));
					o.setName(rs.getString("name"));
					if (image) {
						o.setImage(rs.getBytes("image"));
					}
					list.add(o);
				}
			}
		}
		return list;
	}
}
