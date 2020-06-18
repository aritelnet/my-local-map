package net.aritel.apps.mylocalmap;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

import net.aritel.apps.mylocalmap.db.DB;
import net.aritel.apps.mylocalmap.servlet.AuthenticatedResourceHandler;
import net.aritel.apps.mylocalmap.servlet.LoginServlet;

public class MyLocalMapServer {

	public static Server createServer(int port) throws IOException, URISyntaxException {
		HandlerCollection handlers = new HandlerCollection();
		
		ServletContextHandler context = new ServletContextHandler(handlers, "/", ServletContextHandler.SESSIONS);
		context.getServletHandler().setEnsureDefaultServlet(false);
		
		context.addServlet(LoginServlet.class, "/Login");

		// WebAPIs
		ServletHolder serHol = new ServletHolder(ServletContainer.class);
		serHol.setInitOrder(1);
		serHol.setInitParameter("jersey.config.server.provider.packages", "net.aritel.apps.mylocalmap.servlet.api");
		serHol.setInitParameter(ServerProperties.PROVIDER_SCANNING_RECURSIVE, "true");
		serHol.setInitParameter(ServerProperties.TRACING, "ALL");
		serHol.setInitParameter("jersey.config.server.tracing", "ALL");
		serHol.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
		serHol.setInitParameter("jersey.config.server.provider.classnames", "org.glassfish.jersey.media.multipart.MultiPartFeature");
		context.addServlet(serHol, "/api/*");

		// Static items in authenticated scope.
		ResourceHandler authenticatedResourceHandler = new AuthenticatedResourceHandler();
		if (new File("src/main/webapps-authenticated/").exists())
			authenticatedResourceHandler.setResourceBase("src/main/webapps-authenticated/");
		else
			authenticatedResourceHandler.setResourceBase("../webapps-authenticated/");
		authenticatedResourceHandler.setDirectoriesListed(false);
		authenticatedResourceHandler.setCacheControl("no-store,no-cache,must-revalidate");
		handlers.addHandler(authenticatedResourceHandler);

		// Static images in unauthenticated scope.
		ResourceHandler unauthenticatedResourceHandler = new ResourceHandler();
		if (new File("src/main/webapps-authenticated/").exists())
			unauthenticatedResourceHandler.setResourceBase("src/main/webapps-unauthenticated/");
		else
			unauthenticatedResourceHandler.setResourceBase("../webapps-unauthenticated/");
		unauthenticatedResourceHandler.setDirectoriesListed(false);
		unauthenticatedResourceHandler.setCacheControl("no-store,no-cache,must-revalidate");
		handlers.addHandler(unauthenticatedResourceHandler);
		
		handlers.addHandler(new DefaultHandler());

		// Create server
		Server server = new Server(port);
		server.setHandler(handlers);
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
		try (Connection con = DB.getConnection()) {
		}

		// The use of server.join() the will make the current thread join and
		// wait until the server thread is done executing.
		server.join();

	}
}
