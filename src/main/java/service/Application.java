package service;

import connections.dota.ApiConnector;

public class Application
{
	private static String playerInfosUrl = "https://api.opendota.com/api/players/{account_id}";
	private static String playerWLUrl = "https://api.opendota.com/api/players/{account_id}/wl";
	private static String faiboID = "82383264";
	
	public static void main(String[] args)
	{
		ApiConnector api = new ApiConnector();
		String infosUrl = playerInfosUrl.replace("{account_id}", faiboID);
		
		String[] infos = api.getData(infosUrl).split(":");
		String dotaName = "";
		String accountId = "";
		String level = "";
		boolean name = false;
		boolean id = false;
		boolean lvl = false;
		for (String s : infos)
		{
			if (name)
			{
				dotaName = s.substring(1, s.indexOf(",")-1);
				name = false;
			}
			if (s.contains("personaname"))
			{
				name = true;
			}
			
			if (id)
			{
				accountId = s.substring(0, s.indexOf(","));
				id = false;
			}
			if (s.contains("account_id"))
			{
				id = true;
			}
			
			if (lvl && s.contains(","))
			{
				level = s.substring(0, s.indexOf(","));
				lvl = false;
			}
			if (s.contains("estimate"))
			{
				lvl = true;
			}
		}
		System.out.println(dotaName);
		System.out.println(accountId);
		System.out.println(level);
	}
}
