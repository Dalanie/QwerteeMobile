package melb.msafe.jdispatcher.jdispatch;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

import melb.msafe.jdispatcher.jdispatch.exceptions.HttpError;
import melb.msafe.jdispatcher.jdispatch.exceptions.UnexpectedException;


/**
 * This class handles
 */

public class ResourceHandler {
	
	protected UriPattern uriPattern;
	protected Class<? extends Resource> resourceClass;
	protected Constructor<? extends Resource> resourceConstructor;
	protected HashMap<String, Routine> routines;
	
	public ResourceHandler(UriPattern uriPattern, 
			Class<? extends Resource> resourceClass) {
		this.uriPattern = uriPattern;
		this.resourceClass = resourceClass;
		this.routines = new HashMap<String, Routine>();
		
		// Determine parameter types for Resource class constructor
		Class<?>[] uriParameterTypes = this.uriPattern
				.getParameters().getTypes();
		Class<?>[] parameterTypes = new Class<?>[uriParameterTypes.length + 1];
		parameterTypes[0] = Context.class;
		System.arraycopy(uriParameterTypes, 0, parameterTypes, 1, 
				uriParameterTypes.length);
		
		// Determine Resource class constructor
		try {
			this.resourceConstructor = this.resourceClass
					.getConstructor(parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			// TODO: Implement specific exception type.
			throw new RuntimeException(
					"Resouce constructor params and URI params do not match.");
		}

	}
	
	protected boolean matchesUri(Uri uri) {
		return this.uriPattern.equals(uri);
	}
	
	protected void handle(Query query) throws IOException, HttpError {
		// Try to determine method without considering optional parameters.
		String signatureStr = query.getRoutineSignature(false).toString();
		Routine routine = this.routines.get(signatureStr);
		
		/* If no method can be determined, try to determine method considering 
		 * optional parameters.
		 */
		if (routine == null) {
			signatureStr = query.getRoutineSignature(true).toString();
			routine = this.routines.get(signatureStr);
		}
		
		if (routine == null) 
			throw new HttpError(
					HttpStatus.C404_NOT_FOUND,
					"Resource has no routine with the signature \"" +
					query.getRoutineSignature(false).toString() + "\"");
		
		Arguments uriArgs = this.uriPattern.parseArguments(query.getUri());
		Object[] constructorArgs = new Object[uriArgs.size() + 1];
		constructorArgs[0] = query.getContext();
		System.arraycopy(uriArgs.toValues(this.uriPattern.getParameters()), 
				0, constructorArgs, 1, uriArgs.size());
		
		Resource resource = null;
		try {
			resource = this.resourceConstructor.newInstance(constructorArgs);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException error) {
			throw new RuntimeException(
					"Instantiation of resource object failed");
		} catch (InvocationTargetException error) {
			Throwable cause = error.getCause();
			if (cause instanceof HttpError) {
				throw (HttpError) cause;
			}
			else {
				throw new UnexpectedException(
						"Exception in constructor of resource object", cause);
			}
		}
		routine.call(query, resource);
	}
	
	protected void registerRoutine(HttpMethod method, String routineName, 
			String[] paramNames, Class<?>[] paramTypes, boolean optionalParams,
			String methodName, boolean simpleMethod) {
		Parameters parameters = new Parameters(paramNames, paramTypes);
		Routine routine = new Routine(method, routineName, parameters, 
				optionalParams, methodName, simpleMethod, this.resourceClass);
		String signatureStr = routine.getSignature().toString();
		
		// Check if equivalent routine is always registered?
		if (this.routines.containsKey(signatureStr)) {
			// TODO: Implement specific exception type.
			throw new RuntimeException(
					"An equivalent routine is always registered.");
		}
		
		// Register routine
		this.routines.put(routine.getSignature().toString(), routine);
	}
	
	public void registerRoutine(HttpMethod method, String routineName, 
			String[] paramNames, Class<?>[] paramTypes, boolean optionalParams,
			String methodName) {
		this.registerRoutine(method, routineName, paramNames, paramTypes, 
				optionalParams, methodName, false);
	}
	
	/**
	 * Registers a simple routine handler method.
	 * 
	 * @param method
	 * @param routineName
	 * @param paramNames
	 * @param optionalParams
	 * @param methodName
	 */
	public void registerRoutine(HttpMethod method, String routineName, 
			String[] paramNames, boolean optionalParams, String methodName) {
		
		Class<?>[] paramTypes = new Class<?>[paramNames.length];
		Arrays.fill(paramTypes, String.class);
		
		this.registerRoutine(method, routineName, paramNames, paramTypes, 
				optionalParams, methodName, true);
	} 
	
	public void registerRoutine(HttpMethod method, String routineName, 
			String[] paramNames, Class<?>[] paramTypes, String methodName) {
		this.registerRoutine(method, routineName, paramNames, paramTypes, false, 
				methodName);
	}
	
	/**
	 * Registers a simple routine handler method.
	 * 
	 * @param method
	 * @param routineName
	 * @param paramNames
	 * @param methodName
	 */
	public void registerRoutine(HttpMethod method, String routineName, 
			String[] paramNames, String methodName) {
		this.registerRoutine(method, routineName, paramNames, false, methodName);
	}
	
	public void registerRoutine(HttpMethod method, String[] paramNames, 
			Class<?>[] paramTypes, boolean optionalParams, String methodName) {
		this.registerRoutine(method, "", paramNames, paramTypes, optionalParams, 
				methodName);
	}
	
	/**
	 * Registers a simple routine handler method.
	 * 
	 * @param method
	 * @param paramNames
	 * @param optionalParams
	 * @param methodName
	 */
	public void registerRoutine(HttpMethod method, String[] paramNames, 
			boolean optionalParams, String methodName) {
		this.registerRoutine(method, "", paramNames, optionalParams, 
				methodName);
	} 
	
	public void registerRoutine(HttpMethod method, String[] paramNames, 
			Class<?>[] paramTypes, String methodName) {
		this.registerRoutine(method, "", paramNames, paramTypes, methodName);
	}
	
	/**
	 * Registers a simple routine handler method.
	 * 
	 * @param method
	 * @param paramNames
	 * @param methodName
	 */
	public void registerRoutine(HttpMethod method, String[] paramNames, 
			String methodName) {
		this.registerRoutine(method, paramNames, false, methodName);
	}
	
	public void registerRoutine(HttpMethod method, String routineName, 
			String methodName) {
		this.registerRoutine(method, routineName, new String[]{}, 
				new Class<?>[]{}, methodName);
	}
	
	/**
	 * Registers a simple or ordinary routine handler method.
	 * 
	 * @param method
	 * @param routineName
	 * @param methodName
	 * @param simpleMethod
	 */
	public void registerRoutine(HttpMethod method, String routineName, String 
			methodName, boolean simpleMethod) {
		if (simpleMethod) {
			this.registerRoutine(method, routineName, new String[]{}, 
					methodName);
		} else {
			this.registerRoutine(method, routineName, methodName);
		}
	}
	
	public void registerRoutine(HttpMethod method, String methodName) {
		this.registerRoutine(method, "", methodName);
	}
	
	/**
	 * Registers a simple or ordinary routine handler method.
	 * 
	 * @param method
	 * @param methodName
	 * @param simpleMethod
	 */
	public void registerRoutine(HttpMethod method, String methodName, 
			boolean simpleMethod) {
		this.registerRoutine(method, "", methodName, simpleMethod);
	}
}
