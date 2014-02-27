package melb.msafe.jdispatcher.jdispatch;

import java.util.ArrayList;
import java.util.Collections;

public class UriPattern extends Uri {
	
	protected Parameters parameters;

	public UriPattern(String path, Parameters parameters) {
		super(path);
		this.parameters = parameters;
		
		// Check parameters
		if (this.parameters.size() > this.size()) 
			// TODO: Implement specific exception type.
			throw new RuntimeException("Components to less components of URI " +
					"path for to many parameters");
		
		ArrayList<String> uncheckedParamNames = new ArrayList<String>();
		ArrayList<String> checkedParamNames = new ArrayList<String>();
		Collections.addAll(uncheckedParamNames, this.parameters.getNames());
		
		for (String component:this.components) {
			int index = uncheckedParamNames.indexOf(component);
			if (index != -1) {
				// Parameter name was found in unchecked parameter names
				uncheckedParamNames.remove(index);
				checkedParamNames.add(component);
			}
			else {
				// Parameter name is already checked. So name is duplicate.
				if (checkedParamNames.contains(component)) 
					// TODO: Implement specific exception type.
					throw new RuntimeException("Duplicate parameter names not " +
							"allowed in URI path");
			}
		}
		
		if (!uncheckedParamNames.isEmpty()) 
			// TODO: Implement specific exception type.
			throw new RuntimeException("Uri path contains not all specified " +
					"parameters");
		
		
		// Check URI path
		if (!this.isResourceUri()) 
			// TODO: Implement specific exception type.
			throw new RuntimeException("URI path must be a path to a resource, " +
					"not to a routine");
	}
	
	/**
	 * Checks if this UriPattern object is equal to another object.
	 */
	public boolean equals(Object object) {
		if (object instanceof Uri) {
			Uri other = (Uri) object;
			
			if (this.size() == other.size() && 
					this.isResourceUri() == other.isResourceUri()) {
				
				for (int i = 0; i < this.size(); i++) {
					String component = this.getComponent(i);
					String otherComponent = other.getComponent(i);
					
					if (!component.equals(otherComponent)) 
						if (!this.parameters.has(component)) return false;
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Parse the arguments from another Uri object.
	 * 
	 * @param uri Uri object from where the argument will be parsed.
	 * @return Parsed arguments.
	 */
	public Arguments parseArguments(Uri uri) {
		Uri resourceUri = uri.getResourceUri();
		
		if (!this.equals(resourceUri))
			// TODO: Implement specific exception type.
			throw new RuntimeException(
					"URI matches not pattern, so argument can not parsed.");
		
		Arguments arguments = new Arguments();
		
		for (int i = 0; i < this.size(); i++) {
			String component = this.getComponent(i);
			
			if (this.parameters.has(component)) {
				String name = component;
				String valueStr = resourceUri.getComponent(i);
				
				Argument argument = new Argument(name, valueStr);
				arguments.add(argument);
			}
		}
		return arguments;
	}
	
	public Parameters getParameters() {
		return this.parameters;
	}

}
