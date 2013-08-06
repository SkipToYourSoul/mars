package com.zeedoo.mars.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreApiClientConfiguration {
	
	@Value("${core.api.key}")
	private String apiKey;
	
	@Value("${core.api.secret}")
	private String apiSecret;
	
	@Value("${core.api.host}")
	private String baseUrl;
	
	@Bean
	public CoreApiClient coreApiClient() {
		CoreApiClient client = new CoreApiClient(apiKey, apiSecret, baseUrl);
		return client;
	}

}
