package com.qwertee.general;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Read the information of a properties file.
 * 
 * @author Timo Bonanaty
 * 
 */
public class PropertiesReader {
	
	public static final String LOG_LEVEL_PROPERTY = "LogLevel";
	public static final String HTTPS_KEY_STORE_PASSWORD_PROPERTY = "HttpsKeyStorePassword";
	public static final String HTTPS_PORT_PROPERTY = "HttpsPort";
	public static final String HTTP_PORT_PROPERTY = "HttpPort";
	public static final String SESSION_TIMEOUT_PROPERTY = "SessionTimeout";
	
	public static final String LOG_LEVEL_DEFAULT = "ALL";
	public static final String HTTPS_KEY_STORE_PASSWORD_DEFAULT = "123456";
	public static final String HTTPS_PORT_DEFAULT = "9433";
	public static final String HTTP_PORT_DEFAULT = "8090";
	public static final String SESSION_TIMEOUT_DEFAULT = "120";

	private static final String propertiesFilePath = System
			.getProperty("user.home")
			+ File.separator
			+ ServerConstants.PROJECT_FOLDERNAME
			+ File.separator
			+ "mobile4d.properties";

	/**
	 * To get the properties object
	 * 
	 * @return properties object
	 */
	public static Properties getProperties() {
		MSafeLogger.getLogger().fine(
				"getProperties from " + propertiesFilePath);
		return PropertiesReader.loadFile();
	}

	/**
	 * To get one value of the given parameter
	 * 
	 * @param param
	 *            the parameter of the property
	 * @return the value, if it exists in the property file a default value, if
	 *         it don't exists in the property file, but it exists a default
	 *         value else null
	 */
	public static String getProperty(String param) {
		MSafeLogger.getLogger().fine("getProperty " + param);
		Properties property = PropertiesReader.loadFile();

		if (property.getProperty(param) == null) {

			String defaultPropertyValue = createDefaultProperty().getProperty(
					param);

			// A default value for the param exists, but is still not in the
			// file
			// Add this to the file
			if (defaultPropertyValue != null) {
				MSafeLogger.getLogger().info(
						"Add following default property to the property-file: "
								+ param + ": " + defaultPropertyValue);

				property.setProperty(param, defaultPropertyValue);

				try {
					FileOutputStream outputStream = new FileOutputStream(
							propertiesFilePath);
					property.store(outputStream,
							"# Configuration file for the Mobile4D-Server (default value for "
									+ param + " added)");
					outputStream.close();
				} catch (IOException e) {
					MSafeLogger.log(Level.WARNING,
							"Could not add the not existing default property to the property-file: "
									+ param + ": " + defaultPropertyValue, e);
					return defaultPropertyValue;
				}
			}

			return defaultPropertyValue;
		}

		return property.getProperty(param);
	}

	/**
	 * to load a properties file
	 * 
	 * @return the properties loaded from the file
	 */
	private static Properties loadFile() {
		MSafeLogger.getLogger().fine(
				"load PropertyFile " + propertiesFilePath);
		try {
			Properties property = new Properties();
			BufferedInputStream stream = new BufferedInputStream(
					new FileInputStream(propertiesFilePath));
			property.load(stream);
			stream.close();
			return property;

		} catch (FileNotFoundException e) {
			// Create a properties file with default values
			try {
				createPropertyFile();
				MSafeLogger.getLogger().info(
						"Properties File with default values generated");

				Properties property = new Properties();
				BufferedInputStream stream = new BufferedInputStream(
						new FileInputStream(propertiesFilePath));
				property.load(stream);
				stream.close();

				return property;
			}
			// Default properties file cannot created or read
			catch (IOException ioe) {
				return createDefaultProperty();
			}

		} catch (IOException e) {
			// Return default properties values
			return createDefaultProperty();
		}
	}

	/**
	 * To create a properties file with default values
	 * 
	 * @throws IOException
	 *             if the file cannot created
	 */
	public static void createPropertyFile() throws IOException {

		MSafeLogger.getLogger().info(
				"Create new property-File in " + propertiesFilePath);

		File properties = new File(propertiesFilePath);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(properties));
		createDefaultProperty()
				.store(bos,
						"# Configuration file for the Mobile4D-Server (default generated)");
		bos.close();
		MSafeLogger.getLogger().info(
				"A new property-File with default values was generated in "
						+ propertiesFilePath);
	}

	/**
	 * Generate a property with default data.
	 * 
	 * @return the default property
	 */
	private static Properties createDefaultProperty() {

		Properties mSafeProperties = new Properties();

		// Set Properties for Ports
		mSafeProperties.setProperty(HTTP_PORT_PROPERTY, HTTP_PORT_DEFAULT);
		mSafeProperties.setProperty(HTTPS_PORT_PROPERTY, HTTPS_PORT_DEFAULT);

		// Set Property for Https key-store-password
		mSafeProperties.setProperty(HTTPS_KEY_STORE_PASSWORD_PROPERTY, HTTPS_KEY_STORE_PASSWORD_DEFAULT);
		
		// Set Property for Http-Session-Timeout
		mSafeProperties.setProperty(SESSION_TIMEOUT_PROPERTY, SESSION_TIMEOUT_DEFAULT);

		// Set Properties for Logger
		mSafeProperties.setProperty(LOG_LEVEL_PROPERTY, LOG_LEVEL_DEFAULT);

		return mSafeProperties;
	}
}