package service;

import connections.DotaAPIFacade;
import connections.dota.AbstractProfile;
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
		DotaAPIFacade api = new DotaAPIFacade();
		AbstractProfile profile = api.getProfile(username, "");
		System.out.println(	"ID: " + profile.getId() + "\n" + 
							"Name: " + profile.getName() + "\n" + 
							"Solo rank mmr: " + profile.getLevel());
	}
}
