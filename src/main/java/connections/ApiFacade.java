package connections;

import connections.dota.AbstractProfile;
import domain.DotaProfile;
import exceptions.ConnectionException;

public class ApiFacade implements ApiInterface
{
	private AbstractProfile profile;

	public ApiFacade()
	{
		profile = new DotaProfile();
	}

	public AbstractProfile getProfile(String summoner, String region) throws ConnectionException
	{
		return profile.byName(new String[]
		{ summoner }, region);
	}
}
