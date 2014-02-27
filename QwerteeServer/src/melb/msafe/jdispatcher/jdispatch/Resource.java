package melb.msafe.jdispatcher.jdispatch;

/**
 * Resources are classes that will be mapped by corresponding URI's. A class must extend Resource in order to be
 * accepted by the Dispatcher.
 * For more details take a look at ResourceHandler
 */

public abstract class Resource {
	
	private Context context;
	
	public Resource(Context context) {
		this.context = context;
	}
	
	protected Context getContext() {
		return this.context;
	}
	
}
