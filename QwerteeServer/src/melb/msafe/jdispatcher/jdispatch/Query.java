package melb.msafe.jdispatcher.jdispatch;


import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import melb.msafe.jdispatcher.jdispatch.exceptions.HttpError;

import com.google.gson.Gson;

/**
 * This class takes the http-request tears it in its parts and stores the corresponding data so that the data of the
 * request will be easily accessible.
 */

public class Query {
    /** the default Timeout-value for a session. Can be altered with a parameter of the constructor of class Dispatcher
    */
	protected static int SESSION_TIMEOUT = 60 * 120; // 120 minutes
    /** makes sure that encoding will be in UTF-8 (everything tastes better in UTF-8)*/
	private static String RESPONSE_ENCODING = "UTF-8";
	private static int RESPONSE_BUFSIZE = 2 ^ 16;
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
    /** The context that will later be accessible by the corresponding Resource. Any Context and Query object have a
     * bidirectional relationship, which means that both know each other. Query can be accessed from context and the
     * other way around.
     *  */
	protected Context context;
	
	// Will be used to save information on demand
	private HttpMethod method;
	private Uri uri;
	private Arguments arguments;

    /**
     * if no session has been set up to this moment the session between server and user will be created here. Also
     * will the context be created that will later be accessible by the corresponding Resources.
     * @param request the incoming http-request
     * @param response the corresponding http-response
     */
	public Query(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		
		// Get session and create context object.
		HttpSession session = request.getSession();
		if (session.isNew())
			session.setMaxInactiveInterval(Query.SESSION_TIMEOUT);
		this.context = new Context(this, session);
	}

	public Context getContext() {
		return this.context;
	}

    /**
     * Gets the HTTP-method from the request-object
     * @return GET,POST,PUT or DELETE or etc.
     * @throws HttpError if an invalid method was assigned.
     */
	public HttpMethod getMethod() throws HttpError {
		if (this.method != null) return this.method;
		
		String methodStr = this.request.getMethod().toUpperCase();
		try {
			this.method = HttpMethod.valueOf(methodStr);
			return this.method;
		} catch (IllegalArgumentException error) {
			throw new HttpError(
					HttpStatus.C405_METHOD_NOT_ALLOWED,
					"Method \"" + "\" is not supported.");
		}
	}

    /**
     * Gets the used URI of the http-request-object
     * @return the URI of the request-object if it exists.
     */
	public Uri getUri() {
		if (this.uri != null) return this.uri;
		
		String uriStr = this.request.getRequestURI();
		String charEncoding = this.request.getCharacterEncoding();
		if (charEncoding == null) charEncoding = Query.RESPONSE_ENCODING;
		try {
			uriStr = URLDecoder.decode(uriStr, 
					charEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			// TODO: Implement specific exception type.
			throw new RuntimeException("Unsupported Encoding");
		}
		
		this.uri = new Uri(uriStr);
		return this.uri;
	}

    /**
     * Extracts the RoutineSignature of the incoming http-request-object. That means it will tear the URI down to its
     * explicit parameters and save its values in the RoutineSignature
     * @param optionalParams tells if the parameters on the URI are optional or not.
     * @return the Method RoutineSignature
     * @throws HttpError
     */
	public RoutineSignature getRoutineSignature(boolean optionalParams) 
			throws HttpError {
		// Determine routine name
		String routineName;
		if (this.getUri().isResourceUri()) routineName = "";
		else routineName = this.getUri().getRoutineName();
		
		// Determine parameter names
		String[] parameterNames = this.getArguments().getNames();
		
		// Return routine signature
		return new RoutineSignature(this.getMethod(), 
				routineName, parameterNames, optionalParams);
	}

    /**
     *
     * @return
     * @throws HttpError
     */
	public Arguments getArguments() throws HttpError {
		if (this.arguments != null) return this.arguments;
		
		this.arguments = new Arguments();
		for (Enumeration<String> paramNamesE = this.request.getParameterNames(); 
				paramNamesE.hasMoreElements();) {
			
			String paramName = paramNamesE.nextElement();
			if (!paramName.contains(".")) {
				/* TODO: "." check is only a quick fix to prevent adding 
				 * arguments named like file.org.eclipse.jetty.servlet.contentType.
				 * In future version the new method RoutineSignature.match 
				 * should be used in ResourceHandler, which makes this prevention
				 * unnecessary.
				 */
				Argument argument = new RoutineArgument(paramName, this.request);
				this.arguments.add(argument);
			}
		}
		return this.arguments;
	}
	
	protected HttpServletRequest getRequest() {
		return this.request;
	}
	
	protected HttpServletResponse getResponse() {
		return this.response;
	} 
	
	public void send(Object object) throws IOException {
		if (object instanceof HttpError) {
			HttpError payload = (HttpError) object;
			this.send(payload);
			
		} else if (object instanceof File) {
			File payload = (File) object;
			String mimeType = MimeType.OCTET_STREAM_APPLICATION;
			String fileName = payload.getName();
			this.send(mimeType, fileName, payload);
			
		} else if (object instanceof FileLoad) {
			FileLoad payload = (FileLoad) object;
			this.send(payload);
			
		} else if (object instanceof Result) {
			Result payload = (Result) object;
			this.send(payload);
			
		} else if (object == null) {
			// TODO: Check if case is possible
			this.send(HttpStatus.C204_NO_CONTENT);
			
		} else {
			HttpStatus status = HttpStatus.C200_OK;
			String mimeType = MimeType.JSON_APPLICATION;
			String payload = new Gson().toJson(object);
			this.send(status, mimeType, payload);
		}
	}
	
	private void send(String mimeType, String fileName, File payload) 
			throws IOException {
		long fileLength = payload.length();
		if (fileLength < Integer.MIN_VALUE || fileLength > Integer.MAX_VALUE) 
			// TODO: Implement specific exception type.
			throw new RuntimeException("File can not be send because of its length");
		
		ServletOutputStream outStream = this.response.getOutputStream();
		this.response.setContentType(mimeType);
		this.response.setContentLength((int) fileLength);
		
		/* TODO: For further versions of de.uniBremen.server.jdispatcher.jdispatch it's planned to give the 
		 * decision to the user, if the file should send as attachment or not.
		 * 
		 * For now it will be automatically decided by using the file's MIME 
		 * type.
		 */
		if (MimeType.isImageType(mimeType) || MimeType.isTextType(mimeType)) {
			// Send not as attachment
			this.response.setHeader("Content-Disposition", "filename=\""
					+ fileName + "\"");
		} else {
			// Send as attachment
			this.response.setHeader("Content-Disposition", 
					"attachment; filename=\"" + fileName + "\"");
		}
		
		byte[] buffer = new byte[RESPONSE_BUFSIZE];
		DataInputStream inStream = new DataInputStream(
				new FileInputStream(payload));
		
		// read the file's bytes and write them to the response stream
		int length = inStream.read(buffer);
		while (length != -1) {
			outStream.write(buffer, 0, length);
			length = inStream.read(buffer);
		}
		inStream.close();
		outStream.flush();
		outStream.close();
	}
	
	private void send(FileLoad payload) throws IOException {
		File file = payload.getFile();
		String fileName = payload.getName();
		String mimeType = payload.getMimeType();
		this.send(mimeType, fileName, file);
	}
	
	private void send(Result payload) throws IOException {
		this.send(payload.getStatus(), payload.getMimeType(), 
				payload.getPayload());
	}
	
	private void send(HttpStatus status, String mimeType, String payload) 
			throws IOException {
		this.response.setCharacterEncoding(RESPONSE_ENCODING);
		this.response.setContentType(mimeType);
		this.response.setStatus(status.getCode());
		this.response.getWriter().println(payload);
	}
	
	private void send(HttpStatus status) {
		this.response.setStatus(status.getCode());
	}
}
