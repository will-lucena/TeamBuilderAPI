package domain;

import connections.dota.AbstractProfile;
import exceptions.PrivateDataException;

public abstract class Profile
{
	public abstract AbstractProfile byName(String[] strings, String region) throws PrivateDataException;
}
