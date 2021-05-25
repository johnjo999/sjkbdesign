package com.sjkb.controller;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.integration.ip.IpHeaders;

public interface Sender {
    void send(String payload, @Header(IpHeaders.CONNECTION_ID) String connectionId);
    
}