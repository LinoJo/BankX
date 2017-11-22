package de.bankx.server.main;

import org.apache.log4j.*;
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

	final static Logger log = Logger.getLogger(JettyServer.class);

	public static void main(String[] args) throws Exception {
		// LOG4J Einstellungen
		// Pattern Layout
		PatternLayout layout = new PatternLayout();
		String conversionPattern = "%-7p %d [%t] %c %x - %m%n";
		layout.setConversionPattern(conversionPattern);

		// Konsole
		ConsoleAppender consoleAppender = new ConsoleAppender();
		consoleAppender.setLayout(layout);
		consoleAppender.activateOptions();

		// Datei
		FileAppender fileAppender = new FileAppender();
		fileAppender.setFile("C:\\Temp\\BankX_Core_Log.txt");
		fileAppender.setLayout(layout);
		fileAppender.activateOptions();

		// Root Logger
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.addAppender(consoleAppender);
		rootLogger.addAppender(fileAppender);


		// EINSTELLUNGEN
		// Jetty - Serverport
		int port = 9998;
		// Log4j - Debug-Level
		rootLogger.setLevel(Level.DEBUG);

		Server server = new Server(port);

		// REST API
		ResourceConfig resourceConfig = new PackagesResourceConfig("de.bankx.server.rest");
		ServletContextHandler sh = new ServletContextHandler();
		sh.setContextPath("/rest");
		sh.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/*");

		// Angular2
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        resourceHandler.setResourceBase("../de.bankx.server.angular2");

        ContextHandler contextHandler = new ContextHandler("/angular");
		contextHandler.setHandler(resourceHandler);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { contextHandler, sh });
        server.setHandler(handlers);
		try {
			server.start();
			log.info("Jetty Server auf Port " + port + " gestartet");
		} catch (Exception ex){
			log.error(ex);
		}
	}
}
