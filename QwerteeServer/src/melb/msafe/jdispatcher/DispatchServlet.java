package melb.msafe.jdispatcher;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import melb.msafe.jdispatcher.jdispatch.Dispatcher;
import melb.msafe.jdispatcher.jdispatch.FileLoad;
import melb.msafe.jdispatcher.jdispatch.HttpMethod;
import melb.msafe.jdispatcher.jdispatch.ResourceHandler;
import melb.msafe.jdispatcher.jdispatch.exceptions.UnexpectedException;

@SuppressWarnings("serial")
public class DispatchServlet extends HttpServlet {

	private Dispatcher dispatcher;
	private Logger logger;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// Create logger
		this.logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		StreamHandler printOutHandler = new StreamHandler(System.out,
				new SimpleFormatter());
		this.logger.addHandler(printOutHandler);
		this.logger.setLevel(Level.INFO);
		this.logger.info("Logger initialized");

		// Initialize dispatcher
		this.dispatcher = new Dispatcher(this.logger, 120 * 60);

		// GeneralResource and its routines
		ResourceHandler generalHandler = this.dispatcher.registerResource("/",
				GeneralResource.class);

		generalHandler.registerRoutine(HttpMethod.GET, "index");

		generalHandler.registerRoutine(HttpMethod.GET,
				new String[] { "printOut" }, new Class<?>[] { String.class },
				"index");

		generalHandler.registerRoutine(HttpMethod.GET, "optional",
				new String[] { "stringParam", "intParam" }, new Class<?>[] {
						String.class, int.class }, true, "optional");

		generalHandler.registerRoutine(HttpMethod.GET,
				new String[] { "length" }, new Class<?>[] { int.class },
				"index");

		generalHandler.registerRoutine(HttpMethod.GET, "simple",
				"simpleHandler", true);

		// SpecificResource and its routines
		ResourceHandler specificHandler = this.dispatcher.registerResource(
				"/Specific/id/", new String[] { "id" },
				new Class<?>[] { long.class }, SpecificResource.class);

		specificHandler.registerRoutine(HttpMethod.GET, "index");

		specificHandler.registerRoutine(HttpMethod.POST, "upload",
				new String[] { "file" }, new Class<?>[] { FileLoad.class },
				"upload");

		specificHandler.registerRoutine(HttpMethod.GET, "download", "download");

	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		try {
			this.dispatcher.dispatch(request, response);

		} catch (UnexpectedException error) {
			Throwable cause = error.getCause();
			this.logger.warning(cause.getClass().getName() + ": "
					+ cause.getMessage());
		}
	}

}
