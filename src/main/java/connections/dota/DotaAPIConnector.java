package connections.dota;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class DotaAPIConnector
{
	private CloseableHttpClient httpClient = HttpClientBuilder.create().build();

	public String getData(String url)
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
