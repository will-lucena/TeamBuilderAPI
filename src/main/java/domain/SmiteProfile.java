package domain;

import connections.AbstractProfile;
import connections.smite.SmiteAPI;
import exceptions.ConnectionException;

public class SmiteProfile extends AbstractProfile
{
	public SmiteProfile()
	{
		super();
	}

	public SmiteProfile(String name, long id, long level)
	{
		super(name, id, level);
	}

	@Override
	public AbstractProfile byName(String[] strings, String region) throws ConnectionException
	{
		return SmiteAPI.getInstance().buildSmiteProfile(strings[0]);
	}
}
