package melb.msafe.resources;

import java.util.List;

import melb.msafe.jdispatcher.jdispatch.Context;
import melb.msafe.jdispatcher.jdispatch.Resource;

import com.qwertee.network.Network;
import com.qwertee.tees.VoteTee;

public class VoteResource extends Resource {

	public VoteResource(Context context) {
		super(context);
	}

	public List<VoteTee> info() {
		return Network.getVoteTees();
	}

}
