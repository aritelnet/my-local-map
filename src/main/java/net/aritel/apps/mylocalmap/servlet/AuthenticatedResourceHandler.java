package net.aritel.apps.mylocalmap.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ResourceHandler;

/**
 * Process {@link ResourceHandler} if the session logged in.
 * 
 * @author Aritel
 *
 */
public class AuthenticatedResourceHandler extends ResourceHandler {
	
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("isLoggedIn") != null) {
			super.handle(target, baseRequest, request, response);
		}
	}
	
}
