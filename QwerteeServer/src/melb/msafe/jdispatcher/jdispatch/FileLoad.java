package melb.msafe.jdispatcher.jdispatch;

import java.io.File;

public class FileLoad {
	
	private File file;
	private String name;
	private String mimeType;
	
	public FileLoad(File file, String name, String mimeType) {
		this.file = file;
		this.name = name;
		this.mimeType = mimeType;
	}

	public FileLoad(File file, String mimeType) {
		this(file, file.getName(), mimeType);
	}
	
	public FileLoad(File file) {
		this(file, MimeType.OCTET_STREAM_APPLICATION);
	}
	
	public FileLoad(String path, String name, String mimeType) {
		this(FileLoad.loadFile(path), name, mimeType);
	}
	
	public FileLoad(String path, String mimeType) {
		this(FileLoad.loadFile(path), mimeType);
	}
	
	public FileLoad(String path) {
		this(FileLoad.loadFile(path));
	}
	
	private static File loadFile(String path) {
		File file = new File(path);
		if (!file.isFile()) 
			// TODO: Implement specific exception type.
			throw new RuntimeException("Path is not a file path or file" +
					" does not exists");
		else return file;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getMimeType() {
		return this.mimeType;
	}
	
	protected File getFile() {
		return this.file;
	}
	
	/**
	 * Saves or moves file.
	 * @param path
	 */
	public void save(String path) {
		File destFile = new File(path);
		if (this.isSaved()) {
			// The file will be moved
			/*  TODO: Make moving the file platform independent. 
			 *  See: http://stackoverflow.com/questions/300559/move-copy-file-operations-in-java
			 */
			this.file.renameTo(destFile);
			if (this.file.exists()) this.file.delete();
			this.file = destFile;
			
		} else {
			// The file will be saved for the first time
			this.file.renameTo(destFile);
			this.file = destFile;
		}
	}
	
	public boolean isSaved() {
		return this.file.exists();
	}
	
	public void delete() {
		// TODO: Block operations on FileLoad if underlying file is deleted.
		// TODO: Make platform independent. Use java.nio.files.Files delete method.
		boolean deleted = this.file.delete();
		if (!deleted) {
			// TODO: Implement specific exception type.
			throw new RuntimeException("File could not be deleted");
		}
	}
}
