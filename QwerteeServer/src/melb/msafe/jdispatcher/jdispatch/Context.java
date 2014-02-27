package melb.msafe.jdispatcher.jdispatch;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import melb.msafe.jdispatcher.jdispatch.exceptions.HttpError;

/**
 * Every Resource gets a Context-Object that will make all attributes accessible that are important to the Resource.
 */

public class Context {

    /** The Query-Object also knows this context object. this is a bidirectional relationship */
	private Query query;
	private HttpSession session;

    /**
     * This constructor gets called by the Query-Class which gives itself with as parameter.
     * @param query Thats the query-object that instantiates this class.
     * @param session the http-session-object.
     */
	public Context(Query query, HttpSession session) {
		this.query = query;
		this.session = session;
	}

    /**
     * gets the http-method, that was used.
     * @return GET,POST, etc. ...
     * @throws HttpError
     */
	public String getHttpMethod() throws HttpError {
		return this.query.getMethod().toString();
	}

    /**
     * gets the URI with which the corresponding Resource was called.
     * @return the URI.
     */
	public String getUri() {
		return this.query.getUri().toString();
	}


    /**
     * puts a session-attribute to the session-object
     * @param key the key of the session-attribute
     * @param value the value of the session attribute
     */
	public void put(String key, Object value) {
		try {
			this.session.setAttribute(key, value);
		} catch (IllegalStateException error) {
			// TODO: Implement specific exception type.
			throw new RuntimeException("underlying session is invalidate");
		}
	}

    /**
     * removes a session-attribute from the session-object
     * @param key the key of the attribute that should be removed.
     */
	public void remove(String key) {
		try {
			this.session.removeAttribute(key);
		} catch (IllegalStateException error) {
			// TODO: Implement specific exception type.
			throw new RuntimeException("underlying session is invalidate");
		}
	}

    /**
     * gets the value of a specific session-attribute of the session-object
     * @param key the key of the value that should be extracted from the session-object
     * @return the value of the session-attribute
     */
	public Object get(String key) {
		Object value = null;
		try {
			value = this.session.getAttribute(key);
		} catch (IllegalStateException error) {
			// TODO: Implement specific exception type.
			throw new RuntimeException("underlying session is invalidate");
		}
		
		if (value == null) 
			// TODO: Implement specific exception type.
			throw new RuntimeException("no value associated to key");
		
		return value;
	}

    /**
     * tells if a specific session-attribute exists in the session-object
     * @param key the key of the attribute that is asked for
     * @return true if the attribute exists
     */
	public boolean contains(String key) {
		try {
			if (this.session.getAttribute(key) == null) return false;
			else return true;
		} catch (IllegalStateException error) {
			// TODO: Implement specific exception type.
			throw new RuntimeException("underlying session is invalidate");
		}
	}

    /**
     * gets all keys of all attributes stored in the session-object
     * @return all keys of the session-object in an Array
     */
	public String[] getKeys() {
		try {
			ArrayList<String> keys = new ArrayList<String>();
			for (Enumeration<String> keysE = this.session.getAttributeNames(); 
					keysE.hasMoreElements();) {
				String key = keysE.nextElement();
				keys.add(key);
			}
				
			return keys.toArray(new String[keys.size()]);
			
		} catch (IllegalStateException error) {
			// TODO: Implement specific exception type.
			throw new RuntimeException("underlying session is invalidate");
		}
	}
	
	/**
	 * Turn this Context object in a fresh Context object. This means, that all
	 * data which was put to this Context object will be removed.
	 */
	public void clean() {
		try {
			for (String key:this.getKeys()) {
				this.session.removeAttribute(key);
			}
		} catch (IllegalStateException error) {
			// TODO: Implement specific exception type.
			throw new RuntimeException("underlying session is invalidate");
		}
	}
	
	/**
	 * @return True if this Context object is new or was currently cleaned. 
	 */
	public boolean isClean() {
		if (this.getKeys().length == 0) return true;
		else return false;
	}

}
