package connections;

import connections.smite.AbstractProfile;
import domain.SmiteProfile;
import exceptions.ConnectionException;

public class ApiFacade implements ApiInterface
{
	private AbstractProfile profile;

	public ApiFacade()
	{
		profile = new SmiteProfile();
	}

	public AbstractProfile getProfile(String summoner, String region) throws ConnectionException
	{
		return profile.byName(new String[]
		{ summoner }, region);
	}
}
