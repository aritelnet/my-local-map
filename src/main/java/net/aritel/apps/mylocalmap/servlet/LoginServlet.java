package net.aritel.apps.mylocalmap.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.aritel.apps.mylocalmap.db.DB;

/**
 * Process login event.
 * 
 * @author Aritel
 */
@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try (Connection con = DB.getConnection()) {
			
		} catch (SQLException e) {
			throw new ServletException(e);
		}
		// TODO Check login
		
		
		// Now, redirect to root.
		req.getSession().setAttribute("isLoggedIn", true);
		resp.sendRedirect("/");
	}
}
