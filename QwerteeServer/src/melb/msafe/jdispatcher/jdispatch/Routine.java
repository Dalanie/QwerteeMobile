package melb.msafe.jdispatcher.jdispatch;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import melb.msafe.jdispatcher.jdispatch.exceptions.HttpError;
import melb.msafe.jdispatcher.jdispatch.exceptions.UnexpectedException;

public class Routine {
	
	protected Parameters parameters;
	protected boolean optionalParams;
	protected RoutineSignature signature;
	protected Method method;
	protected boolean simpleMethod;
	
	public Routine(HttpMethod method, String name,
			Parameters parameters, boolean optionalParams, String methodName,
			boolean simpleMethod, Class<? extends Resource> resourceClass) {
		this.parameters = parameters;
		this.optionalParams = optionalParams;
		this.signature = new RoutineSignature(method, name, 
				this.parameters.getNames(), this.optionalParams);
		this.simpleMethod = simpleMethod;
		
		Class<?>[] parameterTypes;
		if (this.simpleMethod) {
			// The parameter types of a simple method are a simple question ;-)
			parameterTypes = new Class<?>[]{HttpServletRequest.class, 
					HttpServletResponse.class};
		} else {
			// Determine parameters of method of Resource class
			parameterTypes = this.parameters.getTypes();
		}
			
		// Determine method in Resource class
		try {
			this.method = resourceClass.getMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			/* TODO: Implement specific exception type.
			 * TODO: Neben Methodenname werden auch die Namen der Parameter in 
			 * der Fehlermeldung benoetigt.
			 */
			throw new RuntimeException("Method with name \"" + methodName +
					"\" not found.");
		}
	}

	public RoutineSignature getSignature() {
		return this.signature;
	}

	public void call(Query query, Resource resource) 
			throws IOException, HttpError {
		if (this.simpleMethod) {
			/* Simple methods will be only invoked. The developer of the simple 
			 * method has to take care for parsing the parameters and sending
			 * the results.
			 */
			Object[] arguments = new Object[]{query.getRequest(), 
					query.getResponse()};
			
			try {
				this.method.invoke(resource, arguments);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				// TODO: Implement specific exception type.
				throw new RuntimeException(
						"Invocation of routine handler method failed", e);
			} catch (InvocationTargetException error) {
				Throwable cause = error.getCause();
				if (cause instanceof HttpError) {
					throw (HttpError) cause;
					
				} else {
					throw new UnexpectedException(
							"Exception in handler method", cause);
				}
			}
			
		} else {
			// Determine method arguments
			Object[] arguments = query.getArguments().toValues(this.parameters, 
					this.optionalParams);
			
			// Invoke method on Resource class
			Object result = null;
			try {
				result = this.method.invoke(resource, arguments);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				e.printStackTrace();
				// TODO: Implement specific exception type.
				throw new RuntimeException(
						"Invocation of routine handler method failed");
			} catch (InvocationTargetException error) {
				Throwable cause = error.getCause();
				if (cause instanceof HttpError) {
					throw (HttpError) cause;
					
				} else {
					throw new UnexpectedException(
							"Exception in handler method", cause);
				}
			}
			query.send(result);
		}
	}

}
