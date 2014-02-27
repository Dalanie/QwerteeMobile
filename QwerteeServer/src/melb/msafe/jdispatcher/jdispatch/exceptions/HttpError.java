package melb.msafe.jdispatcher.jdispatch.exceptions;

import melb.msafe.jdispatcher.jdispatch.HttpStatus;
import melb.msafe.jdispatcher.jdispatch.MimeType;
import melb.msafe.jdispatcher.jdispatch.Result;

import com.google.gson.Gson;


public class HttpError extends Exception implements Result {
	
	private class ErrorPayload {
		
		@SuppressWarnings("unused") private int statusCode;
		@SuppressWarnings("unused") private String status;
		@SuppressWarnings("unused") private String message;
		
		public ErrorPayload(HttpError error) {
			// Set HTTP status information
			HttpStatus status = error.getStatus();
			this.statusCode = status.getCode();
			this.status = status.getStatus();
			
			// Set error information
			this.message = error.getMessage();
		}
	}

	private static final long serialVersionUID = 8734067814501235375L;
	private HttpStatus status;
	
	public HttpError(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}
	
	public HttpError(HttpStatus status, String message, Throwable cause) {
		super(message, cause);
		this.status = status;
	}
	
	@Override
	public HttpStatus getStatus() {
		return this.status;
	}
	@Override
	public String getMimeType() {
		return MimeType.JSON_APPLICATION;
	}
	@Override
	public String getPayload() {
		ErrorPayload payload = new HttpError.ErrorPayload(this);
		return new Gson().toJson(payload);
	}
}
