package connections.dota;

import org.json.JSONException;
import org.json.JSONObject;

import connections.AbstractProfile;
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
		if (username.contains(" "))
		{
			throw new ConnectionException("Api não suporta nicks com espaço", username);
		}
		DotaAPIConnector connector = new DotaAPIConnector();
		long steamId = converterUsernameToSteam32Id(username);
		String url = playerInfosUrl.replace("{account_id}", Long.toString(steamId));
		String response = connector.getData(url);
		String name = getName(response);
		long id = getId(response);
		long level = getLevel(response);

		AbstractProfile profile = new DotaProfile(name, id, level);
		return profile;
	}

	private String getName(String json) throws ConnectionException
	{
		if (json.substring(json.indexOf("profile")+9).startsWith("{"))
		{
			JSONObject object = new JSONObject(json.substring(json.indexOf("profile")+9));
			String name = object.getString(nameMarcador);
			
			if (name.equals(nullMarcador))
			{
				throw new ConnectionException("Campo name é privado", json);
			}
			return name;
		}
		throw new ConnectionException("Campo name é privado", json);
	}

	private long getId(String json) throws ConnectionException
	{
		if (json.substring(json.indexOf("profile")+9).startsWith("{"))
		{
			JSONObject object = new JSONObject(json.substring(json.indexOf("profile")+9));
			long id = object.getLong(idMarcador);
			
			if (Long.toString(id).equals(nullMarcador))
			{
				throw new ConnectionException("Campo id é privado", json);
			}
			return id;
		}
		throw new ConnectionException("Campo id é privado", json);
	}

	private long getLevel(String json) throws ConnectionException
	{
		JSONObject object = new JSONObject(json);
		System.out.println(json);
		
		try
		{
			long id = object.getLong(levelMarcador);
			if (Long.toString(id).equals(nullMarcador))
			{
				throw new ConnectionException("Campo solo_rank_mmr é privado", json);
			}
			return id;
		} catch (JSONException ex)
		{
			throw new ConnectionException("Campo solo_rank_mmr é privado", json); 
		}
	}

	private long converterUsernameToSteam32Id(String username) throws ConnectionException
	{
		SteamAPIConnector apiConnector = new SteamAPIConnector();
		return apiConnector.getSteam32Id(username);
	}
}
