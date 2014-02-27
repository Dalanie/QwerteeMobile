package melb.msafe.jdispatcher.jdispatch;

import java.util.ArrayList;
import java.util.HashMap;

public class Parameters {
	
	protected HashMap<String, Class<?>> parameters;
	protected String[] nameOrder;
	
	public Parameters(String[] names, Class<?>[] types) {
		// Check if names and types have the same length
		if (names.length != types.length)
			// TODO: Implement specific exception type.
			throw new RuntimeException("Counts of types and names different");
		
		// Check if there are duplicate names
		ArrayList<String> checkedNames = new ArrayList<String>();
		for (String name:names) {
			if (checkedNames.contains(name))
				// TODO: Implement specific exception type.
				throw new RuntimeException("Name ... is a duplicate");
			checkedNames.add(name);
		}
		
		// All checks successfully absolved - set attributes
		this.nameOrder = names;
		this.parameters = new HashMap<String, Class<?>>();
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			Class<?> type = types[i];
			this.parameters.put(name, type);
		}
	} 
	
	public int size() {
		return this.nameOrder.length;
	}
	
	public boolean has(String name) {
		return this.parameters.containsKey(name);
	}
	
	public Class<?> getType(String name) {
		Class<?> type = this.parameters.get(name);
		if (type != null) return type;
		else 
			// TODO: Implement specific exception type.
			throw new RuntimeException("No corresponding type found for name");
	}
	
	/**
	 * Returns names in that order in which they was passed to the constructor.
	 * @return Parameter names
	 */
	public String[] getNames() {
		return this.nameOrder;
	}
	
	/**
	 * Return types in that order in which their corresponding names was passed
	 * to the constructor.
	 * @return Parameter types
	 */
	public Class<?>[] getTypes() {
		Class<?>[] types = new Class<?>[this.nameOrder.length];
		for (int i = 0; i < this.nameOrder.length; i++) {
			String name = this.nameOrder[i];
			types[i] = this.getType(name);
		}
		return types;
	}

}
