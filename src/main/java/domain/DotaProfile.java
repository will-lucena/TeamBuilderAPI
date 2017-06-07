package domain;

import connections.dota.AbstractProfile;
import connections.dota.ApiConnector;
import exceptions.PrivateDataException;

public class DotaProfile extends Profile
{
	private String playerInfosUrl = "https://api.opendota.com/api/players/{account_id}";
	
	@Override
	public AbstractProfile byName(String[] strings, String region) throws PrivateDataException
	{
		ApiConnector connector = new ApiConnector();
		String url = playerInfosUrl.replace("{account_id}", strings[0]);
		String profileInfos = connector.getData(url);
		String name = getName(profileInfos);
		long id = Long.parseLong(getId(profileInfos));
		long level = Long.parseLong(getLevel(profileInfos));
		
		AbstractProfile profile = new AbstractProfile(name, id, level);
		return profile;
	}
	
	private String getName(String json) throws PrivateDataException 
	{
		String[] infos = json.split(":");
		boolean name = false;
		for (String s : infos)
		{
			if (name)
			{
				//resolver dps
				if (s.equals("null"))
				{
					throw new PrivateDataException("Campo name é privado");
				}
				return s.substring(1, s.indexOf(",")-1);
			}
			if (s.contains("personaname"))
			{
				name = true;
			}
		}
		return null;
	}
	
	private String getId(String json) throws PrivateDataException
	{
		String[] infos = json.split(":");
		boolean id = false;
		for (String s : infos)
		{
			if (id)
			{
				//resolver dps
				if (s.equals("null"))
				{
					throw new PrivateDataException("Campo id é privado");
				}
				return s.substring(0, s.indexOf(","));
			}
			if (s.contains("account_id"))
			{
				id = true;
			}
		}
		return null;
	}
	
	private String getLevel(String json) throws PrivateDataException
	{
		String[] infos = json.split(":");
		boolean lvl = false;
		for (String s : infos)
		{
			if (lvl)
			{
				//resolver dps
				if (s.equals("null"))
				{
					throw new PrivateDataException("Campo level é privado");
				}
				return s.substring(1, s.indexOf(",")-1);
			}
			if (s.contains("solo_competitive_rank"))
			{
				lvl = true;
			}
		}
		return null;
	}
}
