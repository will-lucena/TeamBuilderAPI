package service;

import connections.AbstractProfile;
import connections.ApiInterface;
import connections.dota.DotaAPIFacade;
import connections.smite.SmiteAPIFacade;
import exceptions.ConnectionException;

public class Application
{
	public static void main(String[] args)
	{
		try
		{
			buildDotaPlayer("faibo");
			//buildSmitePlayer("wGordo");
		} catch (ConnectionException e)
		{
			System.out.println("Ocorreu um erro de conexão: " + e.getMessage() + " : " + e.getProblem());
		}
		
	}
	
	public static void buildDotaPlayer(String username) throws ConnectionException
	{
		System.out.println("=== Dota Player ===");
		DotaAPIFacade api = new DotaAPIFacade();
		AbstractProfile profile = api.getProfile(username, "");
		System.out.println(	"ID: " + profile.getId() + "\n" + 
							"Name: " + profile.getName() + "\n" + 
							"Solo mmr: " + profile.getLevel());
	}
	
	public static void buildSmitePlayer(String username) throws ConnectionException
	{
		System.out.println("=== Smite Player ===");
		ApiInterface api = new SmiteAPIFacade();
		AbstractProfile profile = api.getProfile(username, "");
		System.out.println(	"ID: " + profile.getId() + "\n" + 
							"Name: " + profile.getName() + "\n" + 
							"Medium tier: " + profile.getLevel());
	}
}
