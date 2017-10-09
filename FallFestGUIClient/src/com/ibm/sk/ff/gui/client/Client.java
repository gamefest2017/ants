package com.ibm.sk.ff.gui.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Client {
	
	private final String URL;
	
	public Client() {
		URL = "http://" + Config.HOSTNAME + ":" + Config.PORT + "/";
	}
	
	public boolean postMessage(String url, String message) {
		boolean ret = true;
		
		try {
			CloseableHttpClient client = HttpClients.createDefault();
		    HttpPost httpPost = new HttpPost(URL + url);
		 
		    StringEntity se = new StringEntity(message);
		    
		    httpPost.setEntity(se);
		    
		    CloseableHttpResponse response = client.execute(httpPost);
		    
		    if (response.getStatusLine().getStatusCode() != 200) {
		    	ret = false;
		    }
		    
		    client.close();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		
		return ret;
	}
	
	public String getMessage(String url) {
		String responseString = "";
		
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(URL + url);
		 
		    CloseableHttpResponse response = client.execute(httpGet);
		    
		    if (response.getStatusLine().getStatusCode() != 200) {
		    	responseString = null;;
		    } else {
		    	responseString = EntityUtils.toString(response.getEntity());
		    }
		    
		    client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		return responseString;
	}
	

}
