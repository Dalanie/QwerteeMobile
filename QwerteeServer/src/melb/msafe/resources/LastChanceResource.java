package melb.msafe.resources;

import melb.msafe.jdispatcher.jdispatch.Context;
import melb.msafe.jdispatcher.jdispatch.Resource;

import com.qwertee.network.Network;
import com.qwertee.tees.Tee;

public class LastChanceResource extends Resource {

	public LastChanceResource(Context context) {
		super(context);
	}

	public Tee info() {
		return Network.getLastChanceTee();
	}

}
