package melb.msafe.jdispatcher.jdispatch;

public interface Result {
	
	public HttpStatus getStatus();
	public String getMimeType();
	public String getPayload();
}
