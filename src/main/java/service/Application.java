package service;

import connections.dota.AbstractProfile;
import domain.DotaProfile;
import exceptions.ConnectionException;

public class Application
{
	public static void main(String[] args) throws ConnectionException
	{
		String faiboID = "82383264";
		String nullID = "9338651";
		String id64 = "76561198042648992";
		long id = Long.parseLong(id64.substring(3)) - Long.parseLong("61197960265728");
		System.out.println("id32: " + id);
		
		System.out.println();
		
		String[] ids = new String[1];
		ids[0] = faiboID;
		AbstractProfile profile = new DotaProfile().byName(ids, "");
		System.out.println(	"ID: " + profile.getId() + "\n" + 
							"Name: " + profile.getName() + "\n" + 
							"Solo rank mmr: " + profile.getLevel());
	}
}
