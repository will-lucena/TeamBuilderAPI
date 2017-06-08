package service;

import connections.dota.AbstractProfile;
import domain.DotaProfile;
import exceptions.ConnectionException;

public class Application
{
	public static void main(String[] args)
	{
		try
		{
			buildPlayer("faibo");
		} catch (ConnectionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void buildPlayer(String username) throws ConnectionException
	{
		String[] ids = new String[1];
		ids[0] = username;
		AbstractProfile profile = new DotaProfile();
		profile = profile.byName(ids, "");
		System.out.println(	"ID: " + profile.getId() + "\n" + 
							"Name: " + profile.getName() + "\n" + 
							"Solo rank mmr: " + profile.getLevel());
	}
}
