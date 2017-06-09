package domain;

import connections.AbstractProfile;
import connections.dota.DotaAPI;
import exceptions.ConnectionException;

public class DotaProfile extends AbstractProfile
{
	public DotaProfile()
	{
		super();
	}
	
	public DotaProfile(String name, long id, long level)
	{
		super(name, id, level);
	}

	@Override
	public AbstractProfile byName(String[] strings, String region) throws ConnectionException
	{
		return DotaAPI.getInstance().buildDotaPlayer(strings[0]);
	}
}
