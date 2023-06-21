package com.reporte.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.reporte.models.Loggin;
import com.reporte.models.User;

import reactor.core.publisher.Mono;

public class LogginService {
	
	String path1 = "/api/authenticate";
    @Value("${loggin.url}")
    private String url;
    @Value("${loggin.user}")
    private String user;
    @Value("${loggin.pass}")
    private String pass;

    private String tokenid;
   
    
	public String login() {
		if(this.tokenid == null) {

	        WebClient webClient = WebClient.builder()
	                .baseUrl(url+path1)
	                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
	                .build();

	        User body = new User();
	        body.setUsername(user);
	        body.setPassword(pass);

	        Mono<Loggin> responseJson = webClient.post()
	                .body(Mono.just(body), User.class)
	                .retrieve().bodyToMono(Loggin.class);
	        this.tokenid = responseJson.block().getId_token();
	    }
		return this.tokenid;
	}

}
