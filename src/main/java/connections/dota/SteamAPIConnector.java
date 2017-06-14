package connections.dota;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import exceptions.ConnectionException;

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
	
	private String get64Id(String username) throws ConnectionException
	{
		String url = steamUserUrl.replace("{steamKey}", steamKey).replace("{username}", username);
		String response = getData(url);
	
		JSONObject object = new JSONObject(response.substring(response.indexOf("response")+10));
		try
		{
			long id = object.getLong("steamid");
			return Long.toString(id);
		} catch (JSONException ex)
		{
			throw new ConnectionException(ex.getMessage(), ex);
		}
		
	}
	
	private String getData(String url) throws ConnectionException
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
			throw new ConnectionException("Não foi possivel realizar o request a API", e);
		}
		return result;
	}
	
	public long getSteam32Id(String username) throws ConnectionException
	{
		return converter64To32(get64Id(username));
	}
}
