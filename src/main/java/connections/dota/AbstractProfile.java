package connections.dota;

import exceptions.ConnectionException;

public abstract class AbstractProfile
{
	private String name;
	private long id;
	private long level;
	public static final String playerInfosUrl = "https://api.opendota.com/api/players/{account_id}";
	public static final String playerWLUrl = "https://api.opendota.com/api/players/{account_id}/wl";

	public AbstractProfile()
	{
		
	}
	
	public AbstractProfile(String name, long id, long level)
	{
		this.name = name;
		this.id = id;
		this.level = level;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getLevel()
	{
		return level;
	}

	public void setLevel(long level)
	{
		this.level = level;
	}

	public abstract AbstractProfile byName(String[] strings, String region) throws ConnectionException;
}
