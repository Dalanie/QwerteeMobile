package melb.msafe.jdispatcher;

import melb.msafe.jdispatcher.jdispatch.Context;
import melb.msafe.jdispatcher.jdispatch.FileLoad;
import melb.msafe.jdispatcher.jdispatch.MimeType;
import melb.msafe.jdispatcher.jdispatch.Resource;


public class SpecificResource extends Resource {
	
	private long id;

	public SpecificResource(Context context, long id) {
		super(context);
		this.id = id;
	}
	
	public long index() {
		return this.id;
	}
	
	public FileLoad upload(FileLoad fileLoad) {
		fileLoad.save("test.jpg");
		return fileLoad;
	} 
	
	public FileLoad download() {
		return new FileLoad("test.jpg", MimeType.JPEG_IMAGE);
	}

}
