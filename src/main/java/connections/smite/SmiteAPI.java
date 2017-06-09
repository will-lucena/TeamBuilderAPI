package connections.smite;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

	private static final String tierConquestMarcador = "Tier_Conquest";
	private static final String tierDuelMarcador = "Tier_Duel";
	private static final String tierJoustMarcador = "Tier_Joust";
	private static final String idMarcador = "Id";
	private static final String nameMarcador = "Name";
	private static final String nullMarcador = "null";

	private SmiteAPI()
	{
		this.sessionStart = 0;
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
		String[] response = json.split(",");
		for (String info : response)
		{
			if (info.contains(tierConquestMarcador))
			{
				String level = separarString(info, tierConquestMarcador);

				if (level.equals(nullMarcador))
				{
					throw new ConnectionException("Campo level � privado");
				}
				return Long.parseLong(level);
			}
		}
		throw new ConnectionException("Campo level � privado");
	}

	private long getTierDuel(String json) throws ConnectionException
	{
		String[] response = json.split(",");
		for (String info : response)
		{
			if (info.contains(tierDuelMarcador))
			{
				String level = separarString(info, tierDuelMarcador);

				if (level.equals(nullMarcador))
				{
					throw new ConnectionException("Campo level � privado");
				}
				return Long.parseLong(level);
			}
		}
		throw new ConnectionException("Campo level � privado");
	}

	private long getTierJoust(String json) throws ConnectionException
	{
		String[] response = json.split(",");
		for (String info : response)
		{
			if (info.contains(tierJoustMarcador))
			{
				String level = separarString(info, tierJoustMarcador);

				if (level.equals(nullMarcador))
				{
					throw new ConnectionException("Campo level � privado");
				}
				return Long.parseLong(level);
			}
		}
		throw new ConnectionException("Campo level � privado");
	}

	private String getName(String json) throws ConnectionException
	{
		String[] response = json.split(",");
		for (String info : response)
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
		throw new ConnectionException("Campo name � privado");
	}

	private long getId(String json) throws ConnectionException
	{
		String[] response = json.split(",");
		for (String s : response)
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
		throw new ConnectionException("Campo id � privado");
	}

	private String separarString(String info, String marcador)
	{
		info = info.substring(info.indexOf(marcador));
		return info.split(":")[1];
	}

	private String removerAspas(String info)
	{
		return info.substring(1, info.length() - 1);
	}

	public boolean createSession()
	{
		String url = combine(new String[]
		{ BASE_URL + "createsessionjson", devKey, getSignature("createsession"), getTimestamp() }, "/");
		SmiteAPIConnector connector = new SmiteAPIConnector();

		String response = connector.getData(url);
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

			SmiteAPIConnector connector = new SmiteAPIConnector();
			return connector.getData(url);
		}
		throw new ConnectionException("N�o foi possivel acessar a API");
	}

	public String ping() throws ConnectionException
	{
		if (isSessionValid() || createSession())
		{
			SmiteAPIConnector connector = new SmiteAPIConnector();
			return connector.getData(BASE_URL + "ping" + "json");
		}
		throw new ConnectionException("N�o foi possivel acessar a API");
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

	private String getSignature(String method)
	{
		return getMD5(devKey + method + authKey + getTimestamp());
	}

	private String getMD5(String input)
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
			return null;
		}
	}

	private String getTimestamp()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(new Date());
	}
}
