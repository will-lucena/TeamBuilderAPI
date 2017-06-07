package connections;

import connections.dota.AbstractProfile;

public interface ApiInterface
{
	public AbstractProfile getSummoner(String summoner, String region);
}
