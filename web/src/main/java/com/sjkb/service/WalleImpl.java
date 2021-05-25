package com.sjkb.service;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.sjkb.controller.Broadcaster;
import com.sjkb.controller.TcpGatewayEndpoint;
import com.sjkb.models.WalleDirective;
import com.sjkb.models.WalleTeamObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.Channels;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

@Service
@ApplicationScope
public class WalleImpl implements Walle {

    Map<String, IntegrationFlow> subscribers = new HashMap<>();

    @Autowired
    Broadcaster broadcaster;

    private String id;

    @Override
    public String directive(WalleDirective directive, TcpGatewayEndpoint endpoint) {
        String result = "what?";
        if (directive.verb.equals("subscribe")) {
            result = "Subscribed: " + directive.noun;
            id = directive.noun;
        }
        return result;
    }

    @Override
    public void publish(WalleTeamObject message) {
        Gson gson = new Gson();
        String string = gson.toJson(message);
        broadcaster.send(string + "\r\n");
    }

    @Override
    public boolean waitForSub() {
        return subscribers.isEmpty();
    }

    @Override
    public String getId() {
        if (id == null) {
            id = "toTcp.input";
        }
        return id;
    }

    @Override
    public void setChannel(IntegrationFlow flow) {
        subscribers.put(this.id, flow);

    }

}