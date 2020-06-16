package net.aritel.apps.mylocalmap.servlet.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.gson.Gson;

import net.aritel.apps.mylocalmap.db.DB;
import net.aritel.apps.mylocalmap.db.MapUnit;

@Path("/map-units")
public class MapUnitsApi {
	@Path("/dummy")
	@GET
	public String dummy() {
		return "<html><form action='/api/map-units' method='post'><input type='text' name='name'><input type='submit'></form></html>";
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String createUnit(@FormDataParam("name") String name,
			@FormDataParam(value = "file") InputStream fileStream,
			@FormDataParam(value = "file") FormDataContentDisposition fileDisposition) {
		
		try (Connection con = DB.getConnection()) {
			MapUnit mapUnit = new MapUnit();
			mapUnit.setName(name);
			mapUnit.setImage(toByteArray(fileStream));
			mapUnit.insert(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "your name is " + name;
	}

	private byte[] toByteArray(InputStream fileStream) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			byte[] buf = new byte[4096];
			int len;
			while ((len = fileStream.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String list() {
		try (Connection con = DB.getConnection()) {
			List<MapUnit> list = MapUnit.findAll(con);
			return new Gson().toJson(list);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "[]";
	}

	@GET
	@Path("{id}/image")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public byte[] getImage(@PathParam("id") String id) {
		try (Connection con = DB.getConnection()) {
			MapUnit unit = MapUnit.find(con, Long.parseLong(id));
			return unit.getImage();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
