package melb.msafe.jdispatcher.jdispatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import melb.msafe.jdispatcher.jdispatch.exceptions.HttpError;


/**
 * You can consider this class as the main-class of jdispatcher.
 */

public class Dispatcher {

    /**
     * several handlers can be added to a dispatcher in order to map many URI's
     */
	protected ArrayList<ResourceHandler> resourceHandlers;
    /**
     * it is possible to add your own logger.
     */
	protected Logger logger;
	
	public Dispatcher(Logger logger, int sessionTimeout) {
		this.resourceHandlers = new ArrayList<ResourceHandler>();
		this.logger = logger;
		Query.SESSION_TIMEOUT = sessionTimeout;
	}

	public Dispatcher() {
		this(null, Query.SESSION_TIMEOUT);
	}

    /**
     * This method is the bridge between the server and the jdispatch-api. The incoming HTTP-Request-object is given
     * to this method together with the response-object. Then this api will find the corresponding Resource and
     * execute the called method mapped by the URI.
     * @param request the http-request-object
     * @param response the http-response-object.
     * @throws IOException
     */

	public void dispatch(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		Query query = new Query(request, response);
		
		ResourceHandler handler;
		try {
			// Log the query.
			if (this.logger != null) {
				String logMessage = 
					"Called URI: " + query.getUri().toString() + "; " + 
					"Called routine: " + query.getRoutineSignature(false)
						.toString();
				this.logger.info(logMessage);
			}
			
			handler = this.getResourceHandler(query.getUri());
			handler.handle(query);
			
		} catch (HttpError error) {
			// Log the HTTP error.
			if (this.logger != null) {
				HttpStatus status = error.getStatus();
				String statusStr = status.getCode() + ", " + status.getStatus(); 
				this.logger.info(statusStr + ": " + error.getMessage());
			}
			query.send(error);
		}
	}

    /**
     * This method allows to register a Resource on this dispatcher-object that will be addressed by corresponding
     * incoming http-requests.
     * @param uriPatternStr the uri the resource should be mapped to
     * @param paramNames the list of the names of the parameters that also shall be mapped to this uri
     * @param paramTypes the types of the parameters. [length of both lists must match]
     * @param resourceClass the class-value of the resource that is going to be mapped.
     * @return a ResourceHandler on which routines[methods] can be mapped.
     */
	public ResourceHandler registerResource(String uriPatternStr, 
			String[] paramNames, Class<?>[] paramTypes, 
			Class<? extends Resource> resourceClass) {
		Parameters parameters = new Parameters(paramNames, paramTypes);
		UriPattern pattern = new UriPattern(uriPatternStr, parameters);
		
		if (this.isUriAssigned(pattern))
			// TODO: Implement specific exception type.
			throw new RuntimeException("Uri is allways assigned.");
		
		ResourceHandler handler = new ResourceHandler(pattern, resourceClass);
		this.resourceHandlers.add(handler);
		return handler;
	}

    /**
     * This method allows to register a Resource on this dispatcher-object that will be addressed by corresponding
     * incoming http-requests.
     * @param uriPatternStr the uri the resource should be mapped to
     * @param resourceClass the class-value of the resource that is going to be mapped.
     * @return a ResourceHandler on which routines[methods] can be mapped.
     */
	public ResourceHandler registerResource(String uriPatternStr, 
			Class<? extends Resource> resourceClass) {
		return this.registerResource(uriPatternStr, new String[]{}, 
				new Class<?>[]{}, resourceClass);
	}

    /**
     * Will test if a URI was mapped to a Resource
     * @param uri the uri that should be assigned to a Resource
     * @return true if uri has been assigned.
     */
	private boolean isUriAssigned(Uri uri) {
		for (ResourceHandler handler:this.resourceHandlers) {
			if (handler.matchesUri(uri)) return true;
		}
		return false;
	}

    /**
     * gets the ResourceHandler that was mapped by the given URI
     * @param uri the URI that should be assigned to a Resource
     * @return The corresponding ResourcHandler.
     * @throws HttpError if no Resource is mapped on this URI
     */
	private ResourceHandler getResourceHandler(Uri uri) throws HttpError {
		Uri resourceUri = uri.getResourceUri();
		for (ResourceHandler handler:this.resourceHandlers) {
			if (handler.matchesUri(resourceUri)) return handler;
		}
		// TODO: Implement specific exception type.
		throw new HttpError(
				HttpStatus.C404_NOT_FOUND, 
				"To the URI \"" + resourceUri.toString() + "\" " +
						"is no resource assigned");
	}
}
