package melb.msafe.jdispatcher.jdispatch;

import melb.msafe.jdispatcher.jdispatch.exceptions.HttpError;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Stores an Argument of the http-request
 * look at class RoutineArgument for more details.
 */
public class Argument {

	protected String name;
	protected String valueStr;
	
	// for caching unpacked value
	protected Object value;


	public Argument(String name, String valueStr) {
		this.name = name;
		this.valueStr = valueStr;
	}
	
	public String getName() {
		return this.name;
	}

    /**
     * returns the object named "value" if not null and otherwise the valueStr if the parameter type is of type
     * String. If type is not of String and value is null then this method tries to translate the valueStr to the
     * class-type that was given as parameter.
     * CAUTION: DO NOT USE VERY COMPLEX OBJECTS: example - class Image by javafx would fail for example.
     * @param type the type that should be returned by this method. Can be set to null if value is not null.
     * @return returns value or an instance of a class that corresponds to the given type.
     * @throws HttpError if complex-objects that require specific custom-handlers are stored
     */
	public Object toValue(Class<?> type) throws HttpError {
		if (this.value != null) return this.value;
		
		if (type == String.class) return this.valueStr;
		else {
			try {
                /*
                 * note by the documenter
                 * ... will probably fail for complex objects...
                 * without specific handlers this method will throw Exceptions on rather complex-objects. This is a
                 * limitation that must be mentioned!!!!!!!
                 */
				return new Gson().fromJson(this.valueStr, type);
			} catch (JsonSyntaxException error) {
				throw new HttpError(
						HttpStatus.C400_BAD_REQUEST,
						"invalid value \"" + this.valueStr + "\"" +
						"for argument \"" + this.name + "\".");
			}
		}
	}
}
