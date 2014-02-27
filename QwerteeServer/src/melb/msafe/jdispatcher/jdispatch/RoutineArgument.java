package melb.msafe.jdispatcher.jdispatch;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import melb.msafe.jdispatcher.jdispatch.exceptions.HttpError;



/**
 * This class stores a single parameter of the incoming http-request-object. That means it stores its name and its
 * value in the parameters of this superclass.
 */

public class RoutineArgument extends Argument {
	
	protected HttpServletRequest request;

    /**
     * The Constructor for this class
     * @param name gets the name of the URI-parameter that shall be stored.
     * @param request gets the http-request-object to extract the value of the parameter with the name.
     * @throws HttpError
     */
	public RoutineArgument(String name, HttpServletRequest request) 
			throws HttpError {
		super(name, RoutineArgument.getValueStr(name, request));
		this.request = request;
	}

    /**
     * extracts the value of the given name from the given http-request-object as String-Object
     * [note by the person who documented this: this method looks totally stupid and could haven been solved much
     * more graceful...]
     * @param name the name of the parameter that should be extracted
     * @param request the http-request-object to extract the value.
     * @return the value of the parameter as String-Object
     * @throws HttpError
     */
	private static String getValueStr(String name, HttpServletRequest request) 
			throws HttpError {
		String valueStr = request.getParameter(name);
		if (valueStr != null) return valueStr;
		else
			throw new HttpError(
					HttpStatus.C400_BAD_REQUEST,
					"Expected parameter \"" + name + "\" not found.");
	}


    /**
     * If the value of the parameter was stored this method can be called to extract the corresponding value. In
     * order to extract the value correctly the user should know in advance which kind of value should be extracted.
     * This method distinguishes between two cases. Is the value a File-Object or is it anything else? Depending on
     * this this method will give an appropriate value back to the user.
     * @param type the type of the value that should be known in advance.
     * @return the value of this Argument.
     * @throws HttpError
     */
	public Object toValue(Class<?> type) throws HttpError {
		if (type == File.class) {
			
			if (super.value == null) {
				FileLoad fileLoad = this.unpackFile();
				super.value = fileLoad.getFile();
			}
			return super.value;
			
		} else if (type == FileLoad.class) {
			
			if (super.value == null) super.value = this.unpackFile();
			return super.value;
			
		} else return super.toValue(type);
	}
	
	/**
	 * This method is insecure implemented. This means that the method can not 
	 * detect if an uploaded file was corrupted during the upload process. 
	 * Making the file upload secure is planned for a future version of 
	 * de.uniBremen.server.jdispatcher.jdispatch.
	 *
     * ... is what the developer said and then ran away without fixing this issue... yippie ka yeah mo****er...
     * ... we wouldn't even have the source-codes if he wouldn't have been asked explicitly...
     *
	 * @return Upload
	 * @throws HttpError 
	 */
	private FileLoad unpackFile() throws HttpError {
		// Collect basic file data
		// TODO: Implement working with Part object from Servlet API 3.0
		//Part filePart = null;
		String fileName = null;
		File file = null;
		try {
			//filePart = this.request.getPart(this.name);
			fileName = this.request.getParameter(this.name);
			file = (File) this.request.getAttribute(this.name);
			
			//if (filePart == null || fileName == null || file == null) 
			if (fileName == null || file == null)
				throw new IOException();
			
		} catch (IOException error) {
			throw new HttpError(
					HttpStatus.C400_BAD_REQUEST,
					"Expected file upload not found.");
		}
		
		// Determine file's MIME type
		String fileMimeType = null; //filePart.getContentType();
		if (fileMimeType == null)
			//fileMimeType = new MimetypesFileTypeMap().getContentType(fileName);
			fileMimeType = MimeType.guessType(fileName);
		
		return new FileLoad(file, fileName, fileMimeType);
	}
}
