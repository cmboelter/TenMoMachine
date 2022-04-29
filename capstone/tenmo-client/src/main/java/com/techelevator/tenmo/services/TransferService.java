package com.techelevator.tenmo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import com.techelevator.util.BasicLoggerException;
import org.springframework.http.*;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class TransferService {
    private String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();

    public TransferService( String url ) {
        API_BASE_URL = url;
    }

    public String[] listUsers(AuthenticatedUser currentUser) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(currentUser.getToken());
        HttpEntity<String[]> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(API_BASE_URL + "/users", HttpMethod.GET, entity, String[].class).getBody();
    }

    public String[] transferList(AuthenticatedUser currentUser){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer[]> entity = new HttpEntity<>(httpHeaders);

        String[] transfers = null;
        try{
            transfers = restTemplate.exchange(API_BASE_URL + "/list", HttpMethod.GET, entity, String[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException ex){
            BasicLogger.log(ex.getMessage());
        }
        return transfers;
    }

    public boolean sendBucks(AuthenticatedUser currentUser, Transfer transfer){
       boolean success = false;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, httpHeaders);

        try {
            success = restTemplate.exchange(API_BASE_URL + "/transfer/send", HttpMethod.PUT, entity, Boolean.class).getBody();
        }catch (RestClientResponseException ex){
            BasicLogger.log(ex.getMessage());
        }
        return success;
    }

    public boolean addTransfer(AuthenticatedUser currentUser, Transfer transfer){
        boolean success = false;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, httpHeaders);

        try {
            success = restTemplate.exchange(API_BASE_URL + "/transfer", HttpMethod.POST, entity, Boolean.class).getBody();
        }catch (RestClientResponseException ex){
            BasicLogger.log(ex.getMessage());
        }
        return success;
    }


}
