package melb.msafe.jdispatcher.jdispatch;

import javax.servlet.http.HttpServletResponse;

/**
 * For further explanations about HTTP status codes see:
 * http://www.w3schools.com/tags/ref_httpmessages.asp
 * 
 * @author Helmar Hutschenreuter
 */
public enum HttpStatus {
	
	// 1xx: Information
	C100_CONTINUE(HttpServletResponse.SC_CONTINUE, "Continue"),
	C101_SWITCHING_PROTOCOLS(HttpServletResponse.SC_SWITCHING_PROTOCOLS, "Switching Protocols"),
	C103_CHECKPOINT(103, "Checkpoint"),
	
	// 2xx: Successful
	C200_OK(HttpServletResponse.SC_OK, "OK"),
	C201_CREATED(HttpServletResponse.SC_CREATED, "Created"),
	C202_ACCEPTED(HttpServletResponse.SC_ACCEPTED, "Accepted"),
	C203_NON_AUTHORITATIVE_INFORMATION(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION, "Non-Authoritative Information"),
	C204_NO_CONTENT(HttpServletResponse.SC_NO_CONTENT, "No Content"),
	C205_RESET_CONTENT(HttpServletResponse.SC_RESET_CONTENT, "Reset Content"),
	C206_PARTIAL_CONTENT(HttpServletResponse.SC_PARTIAL_CONTENT, "Partial Content"),
	
	// 3xx: Redirection
	C300_MULTIPLE_CHOICES(HttpServletResponse.SC_MULTIPLE_CHOICES, "Multiple Choices"),
	C301_MOVED_PERMANENTLY(HttpServletResponse.SC_MOVED_PERMANENTLY, "Moved Permanently"),
	C302_FOUND(HttpServletResponse.SC_FOUND, "Found"),
	C303_SEE_OTHER(HttpServletResponse.SC_SEE_OTHER, "See Other"),
	C304_NOT_MODIFIED(HttpServletResponse.SC_NOT_MODIFIED, "Not Modified"),
	C307_TEMPORARY_REDIRECT(HttpServletResponse.SC_TEMPORARY_REDIRECT, "Temporary Redirect"),
	C308_RESUME_INCOMPLETE(308, "Resume Incomplete"),
	
	// 4xx: Client error
	C400_BAD_REQUEST(HttpServletResponse.SC_BAD_REQUEST, "Bad Request"),
	C401_UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"),
	C402_PAYMENT_REQUIRED(HttpServletResponse.SC_PAYMENT_REQUIRED, "Payment Required"),
	C403_FORBIDDEN(HttpServletResponse.SC_FORBIDDEN, "Forbidden"),
	C404_NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "Not Found"),
	C405_METHOD_NOT_ALLOWED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed"),
	C406_NOT_ACCEPTABLE(HttpServletResponse.SC_NOT_ACCEPTABLE, "Not Acceptable"),
	C407_PROXY_AUTHENTICATION_REQUIRED(HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED, "Proxy Authentication Required"),
	C408_REQUEST_TIMEOUT(HttpServletResponse.SC_REQUEST_TIMEOUT, "Request Timeout"),
	C409_CONFLICT(HttpServletResponse.SC_CONFLICT, "Conflict"),
	C410_GONE(HttpServletResponse.SC_GONE, "Gone"),
	C411_LENGTH_REQUIRED(HttpServletResponse.SC_LENGTH_REQUIRED, "Length Required"),
	C412_PRECONDITION_FAILED(HttpServletResponse.SC_PRECONDITION_FAILED, "Precondition Failed"),
	C413_REQUEST_ENTITY_TOO_LARGE(HttpServletResponse.SC_LENGTH_REQUIRED, "Request Entity Too Large"),
	C414_REQUEST_URI_TOO_LONG(HttpServletResponse.SC_REQUEST_URI_TOO_LONG, "Request-URI Too Long"),
	C415_UNSUPPORTED_MEDIA_TYPE(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type"),
	C416_REQUEST_RANGE_NOT_SATISFIABLE(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE, "Requested Range Not Satisfiable"),
	C417_EXPECTATION_FAILED(HttpServletResponse.SC_EXPECTATION_FAILED, "Expectation Failed"),
	
	// 5xx: Server error
	C500_INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"),
	C501_NOT_IMPLEMENTED(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented"),
	C502_BAD_GATEWAY(HttpServletResponse.SC_BAD_GATEWAY, "Bad Gateway"),
	C503_SERVICE_UNAVAILABLE(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Service Unavailable"),
	C504_GATEWAY_TIMEOUT(HttpServletResponse.SC_GATEWAY_TIMEOUT, "Gateway Timeout"),
	C505_HTTP_VERSION_NOT_SUPPORTED(HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED, "HTTP Version Not Supported"),
	C511_NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");
	
	private final int code;
	private final String status;
	
    HttpStatus(int code, String status) { 
    	this.code = code; 
    	this.status = status;
    }
    
    public int getCode() {
    	return this.code; 
    }
    
    public String getStatus() {
    	return this.status;
    }
}
