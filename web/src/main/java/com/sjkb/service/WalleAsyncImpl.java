package com.sjkb.service;

import com.sjkb.models.WalleTeamObject;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WalleAsyncImpl implements WalleAsync {

    private RestTemplate restTemplate;
    HttpEntity<String> entity;

    Logger syslog = Logger.getLogger("walle-asy");
    
    @Autowired
    Walle walle;

    public WalleAsyncImpl() {
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("MjQyMzMwM2QtY2U3OC00YTEzLWIzMmUtMzQ4NThhZDA4NDk0YjExZmMzMDItOGI1_PF84_1eb65fdf-9643-417f-9974-ad72cae0e10f");
        entity = new HttpEntity<String>("parameters", headers);
        
    }

    @Override
    @Async
    public void getMessageId(String messageId) {
        syslog.info("getting message"+ messageId);
        String uri = "https://webexapis.com/v1/messages/"+messageId;
        ResponseEntity<WalleTeamObject> response = restTemplate.exchange(uri, HttpMethod.GET, entity, WalleTeamObject.class);
        syslog.info(String.format("response: %d", response.getStatusCodeValue()));
        if (response.getStatusCode().is2xxSuccessful()) {
            walle.publish(response.getBody());
        }

    }

}