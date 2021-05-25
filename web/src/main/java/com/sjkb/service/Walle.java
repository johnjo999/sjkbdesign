package com.sjkb.service;

import com.sjkb.controller.TcpGatewayEndpoint;
import com.sjkb.models.WalleDirective;
import com.sjkb.models.WalleTeamObject;

import org.springframework.integration.dsl.Channels;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;

public interface Walle {

    public String directive(WalleDirective directive, TcpGatewayEndpoint endpoint);

    public void publish(WalleTeamObject message);

	public boolean waitForSub();

	public String getId();

	public void setChannel(IntegrationFlow flow);
    
}