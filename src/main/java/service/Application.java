package service;

import connections.dota.AbstractProfile;
import domain.DotaProfile;
import exceptions.PrivateDataException;

public class Application
{
	public static void main(String[] args) throws PrivateDataException
	{
		String faiboID = "82383264";
		String nullID = "9338651";
		
		String[] ids = new String[1];
		ids[0] = faiboID;
		AbstractProfile profile = new DotaProfile().byName(ids, "");
		System.out.println(	"ID: " + profile.getId() + "\n" + 
							"Name: " + profile.getName() + "\n" + 
							"Solo rank mmr: " + profile.getLevel());
	}
}
