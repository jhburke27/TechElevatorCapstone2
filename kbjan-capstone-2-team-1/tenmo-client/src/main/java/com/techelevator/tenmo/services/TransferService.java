package com.techelevator.tenmo.services;

import org.springframework.web.client.RestTemplate;

public class TransferService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public TransferService(String url){
        this.baseUrl = url;
    }

}
