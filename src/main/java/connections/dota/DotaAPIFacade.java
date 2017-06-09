package connections.dota;

import connections.AbstractProfile;
import connections.ApiInterface;
import domain.DotaProfile;
import exceptions.ConnectionException;

public class DotaAPIFacade implements ApiInterface
{
	private AbstractProfile profile;

	public DotaAPIFacade()
	{
		profile = new DotaProfile();
	}

	public AbstractProfile getProfile(String summoner, String region) throws ConnectionException
	{
		return profile.byName(new String[]
		{ summoner }, region);
	}
}
