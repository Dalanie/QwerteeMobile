package melb.msafe.resources;

import java.util.List;

import melb.msafe.jdispatcher.jdispatch.Context;
import melb.msafe.jdispatcher.jdispatch.Resource;

import com.qwertee.network.Network;
import com.qwertee.tees.PreviousTee;

public class PreviousResource extends Resource {

	public PreviousResource(Context context) {
		super(context);
	}

	public List<PreviousTee> info() {
		return Network.getPreviousTees();
	}

}
