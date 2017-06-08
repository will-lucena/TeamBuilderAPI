package connections;

import connections.dota.AbstractProfile;
import exceptions.ConnectionException;

public interface ApiInterface
{
	public AbstractProfile getProfile(String summoner, String region) throws ConnectionException;
}
