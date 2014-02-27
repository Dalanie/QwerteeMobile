package melb.msafe.jdispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import melb.msafe.jdispatcher.jdispatch.Context;
import melb.msafe.jdispatcher.jdispatch.HttpStatus;
import melb.msafe.jdispatcher.jdispatch.Resource;
import melb.msafe.jdispatcher.jdispatch.exceptions.HttpError;


public class GeneralResource extends Resource {

	public GeneralResource(Context context) {
		super(context);
	}
	
	public String index() {
		return "Hello world!";
	}
	
	public String index(String printOut) {
		return "Parameter: " + printOut;
	}
	
	public String index(int length) {
		return "Length: " + length;
	}
	
	public Object[] optional(String stringParam, int intParam) {
		return new Object[]{stringParam, intParam};
	}
	
	public long[] filterByGPSLocation(long longitude, long latitude) {
		return new long[]{longitude,  latitude};
	}
	
	public void simpleHandler(HttpServletRequest request, 
			HttpServletResponse response) throws HttpError {
		throw new HttpError(HttpStatus.C200_OK, "Simple handler called.");
	}

}
