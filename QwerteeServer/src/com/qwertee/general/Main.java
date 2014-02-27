package com.qwertee.general;

import java.io.File;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.gson.Gson;
import com.qwertee.network.Network;

public class Main {

	// gets the HTTP_Port 9080
	public static final int HTTP_PORT = new Gson().fromJson(
			PropertiesReader.getProperty(PropertiesReader.HTTP_PORT_PROPERTY),
			int.class);
	// gets the HTTPS-Port 9433
	public static final int SSL_PORT = new Gson().fromJson(
			PropertiesReader.getProperty(PropertiesReader.HTTPS_PORT_PROPERTY),
			int.class);
	// getting the ssl-key-passphrase. 123456
	public static final String SSL_KEYSTORE_PASSWORD = PropertiesReader
			.getProperty(PropertiesReader.HTTPS_KEY_STORE_PASSWORD_PROPERTY);

	public static void main(String[] args) throws Exception {
		WebAppContext root = new WebAppContext();
		/*
		 * A Web Applications context is a variation of ServletContextHandler
		 * that uses the standard layout and web.xml to configure the servlets,
		 * filters and other features:
		 * 
		 * A ServletContextHandler is a specialization of ContextHandler with
		 * support for standard servlets. The following code from
		 * OneServletContext shows three instances of the helloworld servlet
		 * registered with a ServletContextHandler:
		 * 
		 * A ContextHandler is a HandlerWrapper that responds only to requests
		 * that have a URI prefix that matches the configured context path.
		 * Requests that match the context path have their path methods updated
		 * accordingly, and the following optional context features applied as
		 * appropriate:
		 * 
		 * A Handler Wrapper is a handler base class that can be used to daisy
		 * chain handlers together in the style of aspect-oriented programming.
		 * For example, a standard web application is implemented by a chain of
		 * a context, session, security and servlet handlers.
		 * 
		 * src: http://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty [call at:
		 * 2013.10.27]
		 */
		configureWebAppContext(root);

		// Create and configure server
		Server server = new Server();

		// HTTP Configuration
		// HttpConfiguration is a collection of configuration information
		// appropriate for http and https. The default
		// scheme for http is <code>http</code> of course, as the default for
		// secured http is <code>https</code> but
		// we show setting the scheme to show it can be done. The port for
		// secured communication is also set here.
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(8443);
		http_config.setOutputBufferSize(32768);

		// HTTP connector
		// The first server connector we create is the one for http, passing in
		// the http configuration we configured
		// above so it can get things like the output buffer size, etc. We also
		// set the port (8080) and configure an
		// idle timeout.
		ServerConnector http = new ServerConnector(server,
				new HttpConnectionFactory(http_config));
		http.setPort(Integer.parseInt(PropertiesReader
				.getProperty(PropertiesReader.HTTP_PORT_PROPERTY)));
		http.setIdleTimeout(30000);

		// SSL Context Factory for HTTPS and SPDY
		// SSL requires a certificate so we configure a factory for ssl contents
		// with information pointing to what
		// keystore the ssl connection needs to know about. Much more
		// configuration is available the ssl context,
		// including things like choosing the particular certificate out of a
		// keystore to be used.
		// SslContextFactory sslContextFactory = new SslContextFactory();
		// sslContextFactory.setKeyStorePath(jetty_home + "/etc/keystore");
		// sslContextFactory.setKeyStorePassword("OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
		// sslContextFactory.setKeyManagerPassword("OBF:1u2u1wml1z7s1z7a1wnl1u2g");

		// HTTPS Configuration
		// A new HttpConfiguration object is needed for the next connector and
		// you can pass the old one as an
		// argument to effectively clone the contents. On this HttpConfiguration
		// object we add a
		// SecureRequestCustomizer which is how a new connector is able to
		// resolve the https connection before
		// handing control over to the Jetty Server.
		HttpConfiguration https_config = new HttpConfiguration(http_config);
		https_config.addCustomizer(new SecureRequestCustomizer());

		// HTTPS connector
		// We create a second ServerConnector, passing in the http configuration
		// we just made along with the
		// previously created ssl context factory. Next we set the port and a
		// longer idle timeout.

		// ServerConnector https = new ServerConnector(server,
		// new SslConnectionFactory(sslContextFactory,"http/1.1"),
		// new HttpConnectionFactory(https_config));
		ServerConnector https = new ServerConnector(server,
				new HttpConnectionFactory(https_config));
		https.setPort(8443);
		https.setIdleTimeout(500000);

		// Here you see the server having multiple connectors registered with
		// it, now requests can flow into the server
		// from both http and https urls to their respective ports and be
		// processed accordingly by jetty. A simple
		// handler is also registered with the server so the example has
		// something to pass requests off to.

		// Set the connectors
		server.setConnectors(new Connector[] { http, https });

		server.setHandler(root);

		Network.init();
		// // false when deploying
		// Start the server
		server.start();
		// The following will normally not be executed:
		server.join();
	}

	private static void configureWebAppContext(WebAppContext context) {
		context.setContextPath("/");
		// root.setDescriptor("src/WEB-INF/web.xml"); <-- DO NOT DELETE THIS
		// LINE!!!
		String resourceBase = System.getProperty("user.home") + File.separator
				+ ServerConstants.PROJECT_FOLDERNAME + File.separator;
		context.setResourceBase(resourceBase);
		context.setParentLoaderPriority(true);

		// Configure web application

		// Configure servlets
		/*
		 * Servlets are the standard way to provide application logic that
		 * handles HTTP requests. Servlets are like constrained Handlers with
		 * standard ways to map specific URIs to specific servlets.
		 */
		ServletHolder servlet = new ServletHolder();
		servlet = new ServletHolder(new DispatchServlet());
		context.addServlet(servlet, "/*");

		// For all filters
		/*
		 * The following filters are normally used when the developer tries to
		 * avoid using the web.xml file.
		 */
		EnumSet<DispatcherType> dispatcherType = EnumSet.of(
				DispatcherType.REQUEST, DispatcherType.FORWARD,
				DispatcherType.ASYNC);

		// Configure filters
		FilterHolder filter = new FilterHolder();
		filter.setName("FileUploadFilter");
		filter.setClassName("org.eclipse.jetty.servlets.MultiPartFilter");
		filter.setInitParameter("deleteFiles", "true");
		context.addFilter(filter, "/Files/upload", dispatcherType);

		filter = new FilterHolder();
		filter.setName("GzipFilter");
		filter.setClassName("org.eclipse.jetty.servlets.GzipFilter");
		filter.setInitParameter("mimeTypes", "text/html," + "text/plain,"
				+ "text/xml," + "application/xhtml+xml," + "text/css,"
				+ "application/javascript," + "application/json,"
				+ "image/svg+xml");
		context.addFilter(filter, "/*", dispatcherType);

		filter = new FilterHolder();
		filter.setName("CrossOriginFilter");
		filter.setClassName("org.eclipse.jetty.servlets.CrossOriginFilter");
		filter.setInitParameter("allowedMethods", "GET," + "POST," + "PUT,"
				+ "DELETE," + "OPTIONS");
		context.addFilter(filter, "/*", dispatcherType);
	}

}
