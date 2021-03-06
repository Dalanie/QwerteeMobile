package melb.msafe.resources;

import java.util.List;

import melb.msafe.jdispatcher.jdispatch.Context;
import melb.msafe.jdispatcher.jdispatch.Resource;

import com.qwertee.network.Network;
import com.qwertee.tees.Category;

/**
 * This class provides some general functionality such as giving recommendations
 * for diseases and symptoms
 * 
 * @author Carsten Pfeffer, Helmar Hutschenreuter
 */
public class GeneralResource extends Resource {

	public GeneralResource(Context context) {
		super(context);
	}

	public List<Category> info() {
		return Network.getFAQ();
	}

	public void refresh() {
		Network.init();
	}

}