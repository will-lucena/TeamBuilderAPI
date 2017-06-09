package connections;

import connections.dota.AbstractProfile;
import connections.dota.DotaAPIConnector;
import connections.dota.SteamAPIConnector;
import domain.DotaProfile;
import exceptions.ConnectionException;

public class DotaAPI
{
	private static final String levelMarcador = "solo_competitive_rank";
	private static final String idMarcador = "account_id";
	private static final String nameMarcador = "personaname";
	private static final String nullMarcador = "null";
	private static final String playerInfosUrl = "https://api.opendota.com/api/players/{account_id}";
	
	private static DotaAPI instance;
	
	private DotaAPI()
	{
		
	}
	
	public static DotaAPI getInstance()
	{
		if (instance == null)
		{
			synchronized (DotaAPI.class)
			{
				if (instance == null)
				{
					instance = new DotaAPI();
				}
			}
		}
		return instance;
	}
	
	public AbstractProfile buildDotaPlayer(String username) throws ConnectionException
	{
		DotaAPIConnector connector = new DotaAPIConnector();
		long steamId = converterUsernameToSteam32Id(username);
		String url = playerInfosUrl.replace("{account_id}", Long.toString(steamId));
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
		throw new ConnectionException("Campo level é privado");
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
	
	private long converterUsernameToSteam32Id(String username)
	{
		SteamAPIConnector apiConnector = new SteamAPIConnector();
		return apiConnector.getSteam32Id(username);
	}
}
