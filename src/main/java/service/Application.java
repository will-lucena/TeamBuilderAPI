package service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import connections.ApiFacade;
import connections.smite.AbstractProfile;
import exceptions.ConnectionException;

public class Application
{
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, ConnectionException
	{
		buildPlayer("wGordo");
	}
	
	public static void buildPlayer(String username) throws ConnectionException
	{
		ApiFacade api = new ApiFacade();
		AbstractProfile profile = api.getProfile(username, "");
		System.out.println(	"ID: " + profile.getId() + "\n" + 
							"Name: " + profile.getName() + "\n" + 
							"Medium tier: " + profile.getLevel());
	}
}
