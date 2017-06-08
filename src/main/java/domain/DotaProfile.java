package domain;

import connections.dota.AbstractProfile;
import connections.dota.DotaAPIConnector;
import exceptions.ConnectionException;

public class DotaProfile extends AbstractProfile
{
	private static final String levelMarcador = "solo_competitive_rank";
	private static final String idMarcador = "account_id";
	private static final String nameMarcador = "personaname";
	private static final String nullMarcador = "null";
	
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
		DotaAPIConnector connector = new DotaAPIConnector();
		String url = playerInfosUrl.replace("{account_id}", strings[0]);
		String profileInfos = connector.getData(url);
		String name = getName(profileInfos);
		long id = getId(profileInfos);
		long level = getLevel(profileInfos);
		
		AbstractProfile profile = new DotaProfile(name, id, level);
		return profile;
	}
	
	private String getName(String json) throws ConnectionException 
	{
		String[] infos = json.split(",");
		for (String info : infos)
		{
			if (info.contains(nameMarcador))
			{
				String name = separarString(info, nameMarcador);
				if (name.equals(nullMarcador))
				{
					throw new ConnectionException("Campo name é privado");
				}
				return removerAspas(name);
			}
		}
		throw new ConnectionException("Campo name é privado");
	}
	
	private long getId(String json) throws ConnectionException
	{
		String[] infos = json.split(",");
		for (String s : infos)
		{
			if (s.contains(idMarcador))
			{
				String id = separarString(s, idMarcador);
				if (id.equals(nullMarcador))
				{
					throw new ConnectionException("Campo id é privado");
				}
				id = removerAspas(id);
				return Long.parseLong(id);
			}
		}
		throw new ConnectionException("Campo id é privado");
	}
	
	private long getLevel(String json) throws ConnectionException
	{
		String[] infos = json.split(",");
		for (String info : infos)
		{
			if (info.contains(levelMarcador))
			{
				String level = separarString(info, levelMarcador);
				
				if (level.equals(nullMarcador))
				{
					throw new ConnectionException("Campo level é privado");
				}
				
				level = removerAspas(level);
				return Long.parseLong(level);
			}
		}
		return Long.MIN_VALUE;
	}
	
	private String separarString(String info, String marcador)
	{
		info = info.substring(info.indexOf(marcador));
		return info.split(":")[1];
	}
	
	private String removerAspas(String info)
	{
		return info.substring(1, info.length()-1);
	}
}
