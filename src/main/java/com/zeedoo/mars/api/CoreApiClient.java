package com.zeedoo.mars.api;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.zeedoo.commons.hmac.HmacClientFilter;

public class CoreApiClient {

	private WebResource webResource;
	private Client client;
	
	public CoreApiClient(String apiKey, String apiSecret, String baseUrl) {
		client = Client.create();
		client.getProperties().put("api_key", apiKey);
		client.getProperties().put("secret_key", apiSecret);
		client.addFilter(new HmacClientFilter(client.getProviders()));
		webResource = client.resource(baseUrl);
	}
	
	//TODO: Make it extend Entity
	public <T extends Object> T get(String url, Class<T> clazz) {
		ClientResponse response = webResource.path(url).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		// We need to gracefully handle NOT_FOUNDs, eg. returning a NULL object instead of throwing a client error.
		if (Status.NOT_FOUND == response.getClientResponseStatus()) {
			return null;
		} else {
			return response.getEntity(clazz);
		}
	}
	
	public <T extends Object> T getWithQueryParams(String url, Class<T> clazz, MultivaluedMap<String, String> queryParams) {
		return webResource.path(url).queryParams(queryParams).accept(MediaType.APPLICATION_JSON).get(clazz);
	}
	
	public <T extends Object> T put(String url, Class<T> clazz, Object entity) {
		return webResource.path(url).type(MediaType.APPLICATION_JSON).put(clazz, entity);
	}
	
	public <T extends Object> T post(String url, Class<T> clazz, Object entity) {
		return webResource.path(url).type(MediaType.APPLICATION_JSON).post(clazz, entity);
	}
	
	public ClientResponse delete(String url) {
		return webResource.path(url).type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
	}
	

}
