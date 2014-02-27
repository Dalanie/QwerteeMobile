package melb.msafe.jdispatcher.jdispatch;

import java.util.ArrayList;
import java.util.Collections;

public class RoutineSignature {
	
	protected HttpMethod method;
	protected String name;
	protected String[] paramNames;
	protected boolean optionalParams;

	public RoutineSignature(HttpMethod method, String name, 
			String[] paramNames, boolean optionalParams) {
		this.method = method;
		this.name = name;
		this.paramNames = paramNames;
		this.optionalParams = optionalParams;
	}
	
	/**
	 * Generates a string which can be used to compare two RoutineSignature
	 * objects.
	 * 
	 * Example signature string: "POST_routineName(argName1,argName2,argName3)"
	 * The argument names are sorted alphabetically to ensure comparability.
	 * 
	 * @return String which represents this signature.
	 */
	public String toString() {
		// Sort parameter names
		ArrayList<String> paramNames = new ArrayList<String>();
		Collections.addAll(paramNames, this.paramNames);
		Collections.sort(paramNames);
		
		// Generate signature string
		String signature = this.method.toString() + "_" 
				+ this.name + "(";
		
		if (this.optionalParams == false) {
			int lastIndex = paramNames.size() -1;
			for (int i = 0; i < lastIndex + 1; i++) {
				String paramName = paramNames.get(i);
				if (i < lastIndex) signature += paramName + ",";
				else signature += paramName;
			}
		} else {
			signature += "*";
		}
		
		return signature + ")";
	}
}
