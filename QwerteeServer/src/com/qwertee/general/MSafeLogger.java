package com.qwertee.general;


import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.eclipse.persistence.logging.JavaLog;
import org.eclipse.persistence.logging.LogFormatter;

/**
 * The Logger used by the Mobile4D-Server. Logs to <code>System.out</code> and a
 * file. Uses {@link LogFormatter} and {@link java.util.logging.Logger}.
 * 
 * @author UrsBjoernSchmidt
 */
public class MSafeLogger {

	/**
	 * The folder used by the file handler of the logger.
	 */
	private static final String LOG_FOLDER = System.getProperty("user.home")
			+ File.separator + ServerConstants.PROJECT_FOLDERNAME
			+ File.separator;
	/**
	 * The filename pattern used by the file handler of the logger.
	 */
	private static final String LOG_PATTERN = LOG_FOLDER + "Server %g.log";
	private static final int LOG_FILESIZE = 10485760;
	private static final int LOG_FILECOUNT = 10;

	private static final Level STANDARD_LOGLEVEL = Level.ALL;

	private static final String ERROR_INIT_FAILED = "Couldn't initialize file handler.";
	private static final String ERROR_NO_LOGLEVEL = "No log level in properties file.";
	private static final String ERROR_UNKNOWN_LOGLEVEL = "Unknown log level in properties file.";

	private static final String INFO_INIT = "Logging started.";
	private static final String INFO_CLOSE = "Closing logger.";

	private static Logger logger;
	private static StreamHandler streamHandler;
	private static FileHandler fileHandler;

	static {
		init();
	}

	/**
	 * Private constructor as this is an utility class.
	 */
	private MSafeLogger() {
		super();
	}

	/**
	 * Logger initialization.
	 */
	private static void init() {
		logger = Logger.getLogger(JavaLog.TOPLINK_NAMESPACE);
		// determine log level
		String loggerInitErrorMsg = null;
		Level logLevel = STANDARD_LOGLEVEL;
		final String stringInPropertyFile = PropertiesReader
				.getProperty(PropertiesReader.LOG_LEVEL_PROPERTY);
		if (stringInPropertyFile != null) {
			try {
				final Level levelInPropertyFile = Level
						.parse(stringInPropertyFile);
				logLevel = levelInPropertyFile;
			} catch (IllegalArgumentException e) {
				loggerInitErrorMsg = ERROR_UNKNOWN_LOGLEVEL;
			}
		} else {
			loggerInitErrorMsg = ERROR_NO_LOGLEVEL;
		}
		logger.setLevel(logLevel);
		// stream handler (System.out)
		streamHandler = new StreamHandler(System.out, new LogFormatter()) {
			// Auto-flush version of StreamHandler
			@Override
			public synchronized void publish(LogRecord record) {
				super.publish(record);
				flush();
			}
		};
		streamHandler.setLevel(logLevel);
		logger.addHandler(streamHandler);
		// file handler
		try {
			new File(LOG_FOLDER).mkdirs();
			fileHandler = new FileHandler(LOG_PATTERN, LOG_FILESIZE,
					LOG_FILECOUNT, true);
			fileHandler.setLevel(logLevel);
			fileHandler.setFormatter(new LogFormatter());
			logger.addHandler(fileHandler);
		} catch (SecurityException | IOException e) {
			log(Level.SEVERE, ERROR_INIT_FAILED, e);
		}
		// go!
		if (loggerInitErrorMsg != null) {
			logger.warning(loggerInitErrorMsg);
		}
		logger.info(PropertiesReader.LOG_LEVEL_PROPERTY + " = " + logLevel);
		logger.info(INFO_INIT);
	}

	/**
	 * Returns the logger object used to log messages.
	 * 
	 * @return The logger.
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * Closes the logger.
	 */
	public static void close() {
		logger.info(INFO_CLOSE);
		streamHandler.close();
		logger.removeHandler(streamHandler);
		fileHandler.close();
		logger.removeHandler(fileHandler);
	}

	/**
	 * Logs a message associated with a {@link Throwable}. Logs it's stack
	 * trace.
	 * 
	 * @param level
	 *            One of the message level identifiers, e.g., {@code SEVERE}
	 * @param msg
	 *            The string message (or a key in the message catalog)
	 * @param thrown
	 *            {@link Throwable} associated with log message.
	 */
	public static void log(Level level, String msg, Throwable thrown) {
		final String myMsg = (msg == null) ? "null" : msg;
		final String s = myMsg + (myMsg.endsWith(".") ? " " : ". ")
				+ thrown.getClass().getSimpleName() + ": "
				+ thrown.getMessage();
		StringBuilder sb = new StringBuilder(s);
		for (StackTraceElement e : thrown.getStackTrace()) {
			sb.append("\n").append(e);
		}
		LogRecord record = new LogRecord(level, sb.toString());
		if (thrown.getStackTrace().length > 0) {
			final StackTraceElement top = thrown.getStackTrace()[0];
			record.setSourceClassName(top.getClassName());
			record.setSourceMethodName(top.getMethodName());
		}
		logger.log(record);
	}

	public static void log(Level level, String msg) {
		final String myMsg = (msg == null) ? "null" : msg;
		LogRecord record = new LogRecord(level, myMsg);
		logger.log(record);
	}

	public static void info(String msg) {
		log(Level.INFO, msg);
	}

	public static void severe(String msg) {
		log(Level.INFO, msg);
	}

	public static void all(String msg) {
		log(Level.ALL, msg);
	}

	public static void config(String msg) {
		log(Level.CONFIG, msg);
	}

	public static void fine(String msg) {
		log(Level.FINE, msg);
	}

	public static void finer(String msg) {
		log(Level.FINER, msg);
	}

	public static void finest(String msg) {
		log(Level.FINEST, msg);
	}

	public static void off(String msg) {
		log(Level.OFF, msg);
	}

	public static void warning(String msg) {
		log(Level.WARNING, msg);
	}

}
