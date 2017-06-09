package connections.smite;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONObject;

import connections.AbstractProfile;
import domain.SmiteProfile;
import exceptions.ConnectionException;

public class SmiteAPI
{
	private static SmiteAPI instance;

	private static final String devKey = "2097";
	private static final String authKey = "73E527F50A2A43DE89D6660676BB0D93";
	private static final String BASE_URL = "http://api.smitegame.com/smiteapi.svc/";
	private long sessionStart;
	private String sessionId;
	private SmiteAPIConnector connector;
	
	private static final String tierConquestMarcador = "Tier_Conquest";
	private static final String tierDuelMarcador = "Tier_Duel";
	private static final String tierJoustMarcador = "Tier_Joust";
	private static final String idMarcador = "Id";
	private static final String nameMarcador = "Name";
	private static final String nullMarcador = "null";

	private SmiteAPI()
	{
		this.sessionStart = 0;
		this.connector = new SmiteAPIConnector();
	}

	public static SmiteAPI getInstance()
	{
		if (instance == null)
		{
			synchronized (SmiteAPI.class)
			{
				if (instance == null)
				{
					instance = new SmiteAPI();
				}
			}
		}
		return instance;
	}

	public AbstractProfile buildSmiteProfile(String username) throws ConnectionException
	{
		String json = getPlayer(username);

		String name = getName(json);
		long id = getId(json);
		long lvl = buildLevel(json);

		return new SmiteProfile(name, id, lvl);
	}

	private long buildLevel(String json) throws ConnectionException
	{
		long conquest = getTierConquest(json);
		long duel = getTierDuel(json);
		long joust = getTierJoust(json);

		return (conquest + duel + joust) / 3;
	}

	private long getTierConquest(String json) throws ConnectionException
	{
		JSONObject object = new JSONObject(json.substring(1));
		long tier = object.getLong(tierConquestMarcador);
		
		if (Long.toString(tier).equals(nullMarcador))
		{
			throw new ConnectionException("Campo level é privado", json);
		}
		return tier;
	}

	private long getTierDuel(String json) throws ConnectionException
	{
		JSONObject object = new JSONObject(json.substring(1));
		long tier = object.getLong(tierDuelMarcador);
		
		if (Long.toString(tier).equals(nullMarcador))
		{
			throw new ConnectionException("Campo level é privado", json);
		}
		return tier;
	}

	private long getTierJoust(String json) throws ConnectionException
	{
		JSONObject object = new JSONObject(json.substring(1));
		long tier = object.getLong(tierJoustMarcador);
		
		if (Long.toString(tier).equals(nullMarcador))
		{
			throw new ConnectionException("Campo level é privado", json);
		}
		return tier;
	}

	private String getName(String json) throws ConnectionException
	{
		JSONObject object = new JSONObject(json.substring(1));
		String name = object.getString(nameMarcador);
		
		if (name.equals(nullMarcador))
		{
			throw new ConnectionException("Campo name é privado", json);
		}
		return name;
	}

	private long getId(String json) throws ConnectionException
	{
		JSONObject object = new JSONObject(json.substring(1));
		long id = object.getLong(idMarcador);
		
		if (Long.toString(id).equals(nullMarcador))
		{
			throw new ConnectionException("Campo id é privado", json);
		}
		return id;
	}

	private boolean createSession() throws ConnectionException
	{
		String url = combine(new String[]
		{ BASE_URL + "createsessionjson", devKey, getSignature("createsession"), getTimestamp() }, "/");

		String response = this.connector.getData(url);
		setSessionId(response);
		this.sessionStart = System.currentTimeMillis();
		return true;
	}

	public String getPlayer(String player) throws ConnectionException
	{
		if (isSessionValid() || createSession())
		{
			String url = combine(new String[]
			{ BASE_URL + "getplayerjson", devKey, getSignature("getplayer"), this.sessionId, getTimestamp(), player },
					"/");
			return this.connector.getData(url);
		}
		throw new ConnectionException("Não foi possivel acessar a API");
	}

	public String ping() throws ConnectionException
	{
		if (isSessionValid() || createSession())
		{
			return this.connector.getData(BASE_URL + "ping" + "json");
		}
		throw new ConnectionException("Não foi possivel acessar a API");
	}

	private boolean isSessionValid()
	{
		return sessionId != null && Math.abs(System.currentTimeMillis() - sessionStart) <= 15 * 60 * 1000;
	}

	private void setSessionId(String json)
	{
		String id = "";
		for (String s : json.split(","))
		{
			if (s.contains("session_id"))
			{
				id = s.split(":")[1];
			}
		}
		this.sessionId = id.substring(1, id.length() - 1);
	}

	private String combine(String[] items, String delimiter)
	{
		int k = items.length;
		if (k == 0)
			return null;

		StringBuilder builder = new StringBuilder();
		builder.append(items[0]);

		for (int i = 1; i < items.length; i++)
		{
			builder.append(delimiter).append(items[i]);
		}

		return builder.toString();
	}

	private String getSignature(String method) throws ConnectionException
	{
		return getMD5(devKey + method + authKey + getTimestamp());
	}

	private String getMD5(String input) throws ConnectionException
	{
		try
		{
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(input.getBytes());

			byte[] data = messageDigest.digest();

			// Convert from byte to hex
			StringBuilder stringBuffer = new StringBuilder();
			for (byte b : data)
			{
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					stringBuffer.append("0");
				stringBuffer.append(hex);
			}

			return stringBuffer.toString();
		} catch (NoSuchAlgorithmException e)
		{
			throw new ConnectionException("Não foi possivel gerar a signature", e);
		}
	}

	private String getTimestamp()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(new Date());
	}
}
