package melb.msafe.jdispatcher.jdispatch;

import java.util.ArrayList;

public class Uri {

	protected String[] components;
	protected boolean isResourceUri;
	
	/**
	 * Constructs an Uri object from a path string.
	 * 
	 * @param path
	 */
	public Uri(String path) {
		ArrayList<String> componentsList = new ArrayList<String>();
		this.isResourceUri = true;
		
		char[] pathChars = path.toCharArray();
		int pathCharCount = pathChars.length;
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int i = 0; i < pathCharCount; i++) {
			char currentChar = pathChars[i];
			boolean isSeperatorChar = (currentChar == '/');
			boolean isLastChar = (i == pathCharCount -1);
			
			
			if (isSeperatorChar) {
				String component = stringBuilder.toString();
				if (!component.equals("")) 
					stringBuilder = Uri.addComponent(stringBuilder, 
							componentsList);
				if (isLastChar)
					this.isResourceUri = true;
			} else {
				stringBuilder.append(currentChar);
				if (isLastChar) {
					stringBuilder = Uri.addComponent(stringBuilder, 
							componentsList);
					this.isResourceUri = false;
				}	
			}
		}
		this.components = componentsList.toArray(
				new String[componentsList.size()]);
	}
	
	/**
	 * Method is used in constructor's for-loop.
	 * 
	 * @param stringBuilder
	 * @param components
	 * @return
	 */
	private static StringBuilder addComponent(StringBuilder stringBuilder, 
			ArrayList<String> components) {
		String component = stringBuilder.toString();
		components.add(component);
		return new StringBuilder();
	}
	
	/**
	 * Internal constructor. This is used by getResourceUri method.
	 * 
	 * @param components Path components
	 * @param isResourceUri Flag is true if URI represents a resource path
	 */
	private Uri(String[] components, boolean isResourceUri) {
		this.components = components;
		this.isResourceUri = isResourceUri;
	}
	
	/**
	 * Checks if this Uri object is equal to another object.
	 */
	public boolean equals(Object object) {
		if (object instanceof Uri) {
			if (object instanceof UriPattern) {
				UriPattern uriPattern = (UriPattern) object;
				return uriPattern.equals(this);
			} else {
				Uri uri = (Uri) object;
				return this.toString().equals(uri.toString());
			}
		}
		return false;
	}
	
	/**
	 * @return Path string in a distinct format.
	 */	
	public String toString() {
		String path = "/";
		int componentsCount = this.size();
		
		for (int i = 0; i < componentsCount; i++) {
			String component = this.components[i];
			boolean isLastComponent = (i == componentsCount -1);
			
			if (isLastComponent) {
				path += component;
				if (this.isResourceUri) path += "/";
			} else {
				path += component + "/";
			}
		}
		return path;
	}
	
	/**
	 * Returns the count of path components. Several path components are divided
	 * by a slash in the path string.
	 * 
	 * @return The count of path components
	 */
	public int size() {
		return this.components.length;
	}
	
	/** 
	 * @param index Index of a path component.
	 * @return Path component
	 */	
	public String getComponent(int index) {
		try {
			return this.components[index];
		} catch (ArrayIndexOutOfBoundsException error) {
			// TODO: Implement specific exception type.
			throw error;
		}
	}
	
	/**
	 * @return Resource URI object.
	 */
	public Uri getResourceUri() {
		if (this.isResourceUri) {
			return new Uri(this.components, true);
		} else {
			// Remove last component from components array
			int newComponentCount = this.components.length -1;
			String[] newComponents = new String[newComponentCount];
			for (int i = 0; i < newComponentCount; i++) {
				newComponents[i] = this.components[i];
			} 
			return new Uri(newComponents, true);
		}
	}
	
	/**
	 * A URI is the URI of a resource if the routine name is an empty string.
	 * 
	 * @return Returns true if URI is an URI of a resource.
	 */
	public boolean isResourceUri() {
		return this.isResourceUri;
	}
	
	/**
	 * @return The routine name which is the last component of the path.
	 */
	public String getRoutineName() {
		if (!this.isResourceUri()) {
			return this.components[this.components.length - 1];
		} else {
			// TODO: Implement specific exception type.
			throw new RuntimeException("URI is not a routine uri");
		}
	}
}
