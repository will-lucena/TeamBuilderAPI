package domain;

import connections.dota.AbstractProfile;

public abstract class Profile
{
	public abstract AbstractProfile byName(String[] strings, String region);
}
