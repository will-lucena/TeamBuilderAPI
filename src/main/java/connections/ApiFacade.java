package connections;

import connections.dota.AbstractProfile;
import domain.DotaProfile;
import domain.Profile;
import exceptions.PrivateDataException;

public class ApiFacade implements ApiInterface
{
	private Profile profile;

	public ApiFacade()
	{
		profile = new DotaProfile();
	}

	public AbstractProfile getSummoner(String summoner, String region) throws PrivateDataException
	{
		return profile.byName(new String[]
		{ summoner }, region);
	}
}
