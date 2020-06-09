package net.aritel.apps.mylocalmap;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionIdManager;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import net.aritel.apps.mylocalmap.db.DB;
import net.aritel.apps.mylocalmap.servlet.AuthenticatedResourceHandler;
import net.aritel.apps.mylocalmap.servlet.LoginServlet;

public class MyLocalMapServer {

	public static Server createServer(int port) throws IOException, URISyntaxException {
		// Note that if you set this to port 0 then a randomly available port
		// will be assigned that you can either look in the logs for the port,
		// or programmatically obtain it for use in test cases.
		Server server = new Server(port);

		ResourceHandler authenticatedResourceHandler = new AuthenticatedResourceHandler();
		authenticatedResourceHandler.setResourceBase("src/main/webapps-authenticated/");
		authenticatedResourceHandler.setDirectoriesListed(false);
		authenticatedResourceHandler.setCacheControl("no-store,no-cache,must-revalidate");

		ResourceHandler unauthenticatedResourceHandler = new ResourceHandler();
		unauthenticatedResourceHandler.setResourceBase("src/main/webapps-unauthenticated/");
		unauthenticatedResourceHandler.setDirectoriesListed(false);
		unauthenticatedResourceHandler.setCacheControl("no-store,no-cache,must-revalidate");

		// The ServletHandler is a dead simple way to create a context handler
		// that is backed by an instance of a Servlet.
		// This handler then needs to be registered with the Server object.
		ServletHandler handler = new ServletHandler();

		// Passing in the class for the Servlet allows jetty to instantiate an
		// instance of that Servlet and mount it on a given context path.

		// IMPORTANT:
		// This is a raw Servlet, not a Servlet that has been configured
		// through a web.xml @WebServlet annotation, or anything similar.
		handler.addServletWithMapping(LoginServlet.class, "/Login");
		handler.setEnsureDefaultServlet(false);
		handler.setHandler(new SessionHandler());

		HandlerCollection handlers = new HandlerCollection(handler, authenticatedResourceHandler,
				unauthenticatedResourceHandler, new DefaultHandler());
		server.setHandler(handlers);

		// Specify the Session ID Manager
		SessionIdManager idmanager = new DefaultSessionIdManager(server);
		server.setSessionIdManager(idmanager);

		return server;
	}

	public static void main(String[] args) throws Exception {
		ArgsMap argsMap = new ArgsMap(args);
		// Create a basic jetty server object that will listen on port 8080.
		int port = argsMap.getInt("jetty.http.port", 8080);
		Server server = createServer(port);

		// Start things up!
		server.start();

		// TODO dummy
		try (Connection con = DB.getConnection()) {}

		// The use of server.join() the will make the current thread join and
		// wait until the server thread is done executing.
		server.join();
		
	}
}
