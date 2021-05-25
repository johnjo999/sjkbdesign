package com.sjkb.controller;

import com.sjkb.models.WalleDirective;
import com.sjkb.service.Walle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.dsl.Channels;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

@MessageEndpoint
public class TcpGatewayEndpoint {

    @Autowired
    Walle walle;

    private String inbuff = "";

    @Transformer(inputChannel = "fromTcp", outputChannel = "toEcho")
    public String convert(byte[] bytes) {
        String response = "?";
        inbuff = new String(bytes);
        Logger.getAnonymousLogger().log(Level.INFO, () -> String.format("message: %s", inbuff));
        if (inbuff.startsWith("{") && bytes[bytes.length - 1] == '}') {
            Gson gson = new Gson();
            WalleDirective directive = gson.fromJson(inbuff.trim(), WalleDirective.class);
            if (directive != null)
                response = walle.directive(directive, this);
            inbuff = "";
        }
        return response;
    }

    @ServiceActivator(inputChannel = "toEcho")
    public String toClient(String in) {
        return in;
    }

    @Transformer(inputChannel = "resultToString")
    public String convertResult(byte[] bytes) {
        return new String(bytes);
    }

	public String channel() {
		return walle.getId();
	}

	public void stashChannel(IntegrationFlow flow) {
        walle.setChannel(flow);
    }

}
