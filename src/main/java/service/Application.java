package service;

import connections.AbstractProfile;
import connections.dota.DotaAPIFacade;
import exceptions.ConnectionException;

public class Application
{
	public static void main(String[] args)
	{
		try
		{
			buildDotaPlayer("Godot");
		} catch (ConnectionException e)
		{
			System.out.println("Ocorreu um erro de conexão: " + e.getProblem());
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
}
