package connections.smite;

import connections.AbstractProfile;
import connections.ApiInterface;
import domain.SmiteProfile;
import exceptions.ConnectionException;

public class SmiteAPIFacade implements ApiInterface
{
	private AbstractProfile profile;

	public SmiteAPIFacade()
	{
		profile = new SmiteProfile();
	}

	public AbstractProfile getProfile(String summoner, String region) throws ConnectionException
	{
		return profile.byName(new String[]
		{ summoner }, region);
	}
}
