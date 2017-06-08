package connections.dota;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class SteamAPIConnector
{
	private static final String steamKey = "C0A26A72E4EC723F45C3EA9543B7B7F1";
	private static final String steamUserUrl = 
			"http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key={steamKey}&vanityurl={username}";
	private CloseableHttpClient httpClient = HttpClientBuilder.create().build();
	
	private long converter64To32(String steam64Id)
	{
		return Long.parseLong(steam64Id.substring(3)) - Long.parseLong("61197960265728");
	}
	
	private String get64Id(String username)
	{
		String url = steamUserUrl.replace("{steamKey}", steamKey).replace("{username}", username);
		String infos = getData(url);
		
		String id = infos.substring(infos.indexOf("steamid"), infos.indexOf(","));
		id = id.split(":")[1];
		return id.substring(2, id.length()-1);
	}
	
	private String getData(String url)
	{
		String result = "";
		HttpGet request = new HttpGet(url);
		request.addHeader("content-type", "application/json");
		try
		{
			CloseableHttpResponse httpResponse = httpClient.execute(request);
			System.out.println("status code: " + httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200)
			{

				result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			}

		} catch (ParseException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public long getSteam32Id(String username)
	{
		return converter64To32(get64Id(username));
	}
}

