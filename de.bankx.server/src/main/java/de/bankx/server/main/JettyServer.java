package de.bankx.server.main;

import de.bankx.server.services.DatabaseService;
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

import java.sql.*;

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
        resourceHandler.setResourceBase("../de.bankx.server.angular");

        ContextHandler contextHandler = new ContextHandler("/angular");
		contextHandler.setHandler(resourceHandler);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { contextHandler, sh });
        server.setHandler(handlers);
		try {
			server.start();
			log.info("Jetty Server auf Port " + port + " gestartet");

			// Datenbank-Defaults setzen, falls erster Start der Anwendung
			dbdefaults();
		} catch (Exception ex){
			log.error(ex);
		}
	}

	private static void dbdefaults(){
		// Datenbank-Defaults-Setzen
		try {
			Connection con = DatabaseService.getInstance().getConnection();

			// Überprüfen ob Tabellen bereits angelegt (erster Start)
			DatabaseMetaData dbm = con.getMetaData();
			ResultSet tables = dbm.getTables(null, null, "ACCOUNTS", null);
			if (tables.next()) {
				// Tabelle existiert bereits
				log.debug("Die Datenbank wurde bereits erstellt.");
			}
			else {
				// Tabelle existiert nicht
				log.info("Erster Start - Tabellen werden in Datenbank erstellt");

				Statement stmt = con.createStatement();
				stmt.execute("CREATE TABLE Accounts(" +
						"id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
						"owner CHAR(50) NOT NULL," +
						"number CHAR(20) NOT NULL," +
						"CONSTRAINT primary_key PRIMARY KEY (id)," +
						"UNIQUE (number)" +
						")"
				);
				log.info("Tabelle Accounts angelegt");
				stmt.executeUpdate("INSERT INTO Accounts(owner , number) values('Bank' , '0000')");
				log.info("owner 'Bank' mit number '0000' zu Accounts hinzugefügt");
				stmt.executeUpdate("INSERT INTO Accounts(owner , number) values('Timon' , '1000')");
				log.info("owner 'Timon' mit number '1000' zu Accounts hinzugefügt");
			}

			con.close();
		} catch (SQLException e) {
			log.error("SQLException dbdefaults(): " + e.getMessage());
		}
	}
}
