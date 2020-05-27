package net.aritel.apps.mylocalmap.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Process login event.
 * 
 * @author Aritel
 */
@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Check login
		
		
		// Now, redirect to root.
		req.getSession().setAttribute("isLoggedIn", true);
		resp.sendRedirect("/");
	}
}
