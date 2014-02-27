package melb.msafe.jdispatcher.jdispatch;

import java.util.HashMap;

public final class MimeType {
	private static final HashMap<String, String> EXTENSION_MAP = 
			MimeType.createExtensionMap();
	private static final String TYPE_APPLICATION = "application";
	private static final String TYPE_IMAGE = "image";
	private static final String TYPE_TEXT = "text";
	
	// Most common used application types
	public static final String JSON_APPLICATION = "application/json";
	public static final String JAVASCRIPT_APPLICATION = "application/javascript";
	public static final String OCTET_STREAM_APPLICATION = "application/octet-stream";
	public static final String PDF_APPLICATION = "application/pdf";
	public static final String XML_APPLICATION = "application/xml";
	public static final String ZIP_APPLICATION = "application/zip";
	public static final String GZIP_APPLICATION = "application/gzip";
	
	// Most common used image types
	public static final String GIF_IMAGE = "image/gif";
	public static final String JPEG_IMAGE = "image/jpeg";
	public static final String PNG_IMAGE = "image/png";
	public static final String SVG_IMAGE = "image/svg+xml";
	public static final String TIFF_IMAGE = "image/tiff";
	
	// Most common used text types
	public static final String CSS_TEXT = "text/css";
	public static final String CSV_TEXT = "text/csv";
	public static final String HTML_TEXT = "text/html";
	public static final String PLAIN_TEXT = "text/plain";
	
	private static HashMap<String, String> createExtensionMap() {
		HashMap<String, String> extMap = new HashMap<String, String>();
		
		// Map application types
		extMap.put("json", MimeType.JSON_APPLICATION);
		extMap.put("js", MimeType.JAVASCRIPT_APPLICATION);
		extMap.put("xml", MimeType.XML_APPLICATION);
		extMap.put("pdf", MimeType.PDF_APPLICATION);
		extMap.put("xml", MimeType.XML_APPLICATION);
		extMap.put("zip", MimeType.ZIP_APPLICATION);
		extMap.put("gz", MimeType.GZIP_APPLICATION);
		
		// Map image types
		extMap.put("gif", MimeType.GIF_IMAGE);
		extMap.put("jpg", MimeType.JPEG_IMAGE);
		extMap.put("jpeg", MimeType.JPEG_IMAGE);
		extMap.put("jpe", MimeType.JPEG_IMAGE);
		extMap.put("jif", MimeType.JPEG_IMAGE);
		extMap.put("jfif", MimeType.JPEG_IMAGE);
		extMap.put("jfi", MimeType.JPEG_IMAGE);
		extMap.put("png", MimeType.PNG_IMAGE);
		extMap.put("svg", MimeType.SVG_IMAGE);
		extMap.put("svgz", MimeType.SVG_IMAGE);
		extMap.put("tiff", MimeType.TIFF_IMAGE);
		extMap.put("tif", MimeType.TIFF_IMAGE);
		
		// Map text types
		extMap.put("css", MimeType.CSS_TEXT);
		extMap.put("csv", MimeType.CSV_TEXT);
		extMap.put("html", MimeType.HTML_TEXT);
		extMap.put("htm", MimeType.HTML_TEXT);
		extMap.put("txt", MimeType.PLAIN_TEXT);
		
		return extMap;
	}

	public static String guessType(String fileName) {
		String[] nameParts = fileName.split("\\.");
		int partCount = nameParts.length;
		
		if (partCount > 0) {
			String fileExt = nameParts[partCount - 1];
			String mimeType = MimeType.EXTENSION_MAP.get(fileExt);
			if (mimeType != null) return mimeType;
		}	
		return MimeType.OCTET_STREAM_APPLICATION;
	}
	
	private static boolean isOfType(String mimeType, String type) {
		String[] mimeTypeParts = mimeType.split("/");
		int partCount = mimeTypeParts.length;
		
		if (partCount > 0) {
			String typePart = mimeTypeParts[0];
			if (type.equals(typePart)) return true;
		}
		return false;
	}
	
	public static boolean isApplicationType(String mimeType) {
		return MimeType.isOfType(mimeType, MimeType.TYPE_APPLICATION);
	}
	
	public static boolean isImageType(String mimeType) {
		return MimeType.isOfType(mimeType, MimeType.TYPE_IMAGE);
	}
	
	public static boolean isTextType(String mimeType) {
		return MimeType.isOfType(mimeType, MimeType.TYPE_TEXT);
	}
	
}
