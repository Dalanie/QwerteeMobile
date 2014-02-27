package melb.msafe.jdispatcher.jdispatch;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Defaults;

import melb.msafe.jdispatcher.jdispatch.exceptions.HttpError;


public class Arguments {
	
	protected HashMap<String, Argument> arguments;
	protected ArrayList<String> nameOrder;

	public Arguments(Argument[] arguments) {
		this.arguments = new HashMap<String, Argument>();
		this.nameOrder = new ArrayList<String>();
		
		for (Argument argument:arguments) {
			this.add(argument);
		}
	}
	
	public Arguments() {
		this(new Argument[]{});
	}
	
	public void add(Argument argument) {
		String name = argument.getName();
		
		if (this.has(name)) 
			// TODO: Implement specific exception type.
			throw new RuntimeException("Argument name is always taken");
		
		this.nameOrder.add(name);
		this.arguments.put(name, argument);
	}
	
	private boolean has(String name) {
		return this.arguments.containsKey(name);
	}
	
	/**
	 * @return Return argument names. The Order is that, in which the arguments 
	 * were added.
	 */
	public String[] getNames() {
		return this.nameOrder.toArray(new String[this.nameOrder.size()]);
	}
	
	public int size() {
		return this.nameOrder.size();
	}
	
	public Object[] toValues(Parameters parameters, boolean optionalParams) 
		throws HttpError {
		
		String[] names = parameters.getNames();
		Class<?>[] types = parameters.getTypes();
		
		Object[] values = new Object[parameters.size()];
		
		for (int i = 0; i < parameters.size(); i++) {
			String name = names[i];
			Class<?> type = types[i];
			
			
			Argument argument = this.arguments.get(name);
			if (argument == null && !optionalParams)
				throw new HttpError(
						HttpStatus.C400_BAD_REQUEST,
						"There is no argument matching the parameter \"" + name
						+ "\".");
			
			
			
			if (argument != null) values[i] = argument.toValue(type);
			else values[i] = Defaults.defaultValue(type);
		}
		
		return values;
	}
 	
	/**
	 * Creates an array of all argument values of the given parameters.
	 * 
	 * @param parameters
	 * @return Argument values
	 * @throws HttpError 
	 */
	public Object[] toValues(Parameters parameters) throws HttpError {
		return this.toValues(parameters, false);
	}
}
