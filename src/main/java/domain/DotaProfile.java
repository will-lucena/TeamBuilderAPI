package domain;

import connections.dota.AbstractProfile;
import connections.dota.ApiConnector;

public class DotaProfile extends Profile
{
	private String playerInfosUrl = "https://api.opendota.com/api/players/{account_id}";
	
	@Override
	public AbstractProfile byName(String[] strings, String region)
	{
		ApiConnector connector = new ApiConnector();
		String url = playerInfosUrl.replace("{account_id}", strings[0]);
		String profileInfos = connector.getData(url);
	}

}
