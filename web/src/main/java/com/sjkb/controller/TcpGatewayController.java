package com.sjkb.controller;

import com.google.gson.Gson;
import com.sjkb.entities.WebHookEntity;
import com.sjkb.entities.WebHookResponse;
import com.sjkb.service.WalleAsync;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/team/")
public class TcpGatewayController {

    @Autowired
    WalleAsync walleAsync;


    @PostMapping(value = "room")
    public WebHookResponse webHook(@RequestBody final WebHookEntity webhook) {
        WebHookResponse response = new WebHookResponse();
        Logger syslog = Logger.getLogger("webhook");
        Gson gson = new Gson();
        String g = gson.toJson(webhook, WebHookEntity.class);
        syslog.log(Level.INFO, g);
        response.setStatus("success");
        walleAsync.getMessageId(webhook.getData().getId());
        return response;
    }
    
}