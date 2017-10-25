package de.bankx.server.main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class JettyServer {

	public static void main(String[] args) throws Exception {
		Server server = new Server(9998);

		// JERSEY
		ResourceConfig resourceConfig = new PackagesResourceConfig("de.fhdw.server.example.rest");
		ServletContextHandler sh = new ServletContextHandler();
		sh.setContextPath("/rest");
		sh.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/*");

		// Angular2
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        resourceHandler.setResourceBase("../de.bankx.client.angular2.start");

        ContextHandler contextHandler = new ContextHandler("/angular");
		contextHandler.setHandler(resourceHandler);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { contextHandler, sh });
        server.setHandler(handlers);

		server.start();
	}
}
