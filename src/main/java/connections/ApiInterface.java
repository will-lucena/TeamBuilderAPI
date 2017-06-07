package connections;

import connections.dota.AbstractProfile;
import exceptions.PrivateDataException;

public interface ApiInterface
{
	public AbstractProfile getSummoner(String summoner, String region) throws PrivateDataException;
}
