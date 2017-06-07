package connections;

import connections.dota.AbstractProfile;
import exceptions.PrivateDataException;

public interface ApiInterface
{
	public AbstractProfile getProfile(String summoner, String region) throws PrivateDataException;
}
