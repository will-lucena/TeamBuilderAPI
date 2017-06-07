package connections;

import connections.dota.AbstractProfile;
import domain.DotaProfile;
import domain.Profile;

public class ApiFacade implements ApiInterface
{
	private Profile profile;

	public ApiFacade()
	{
		profile = new DotaProfile();
	}

	public AbstractProfile getSummoner(String summoner, String region)
	{
		return profile.byName(new String[]
		{ summoner }, region);
	}
}
