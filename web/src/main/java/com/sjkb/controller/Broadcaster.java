package com.sjkb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
    @DependsOn("gateway") // Needed to ensure the gateway flow bean is created first
    @ApplicationScope
	public class Broadcaster {

		@Autowired
		private Sender sender;

		@Autowired
		private AbstractServerConnectionFactory server;

		public void send(String what) {
			this.server.getOpenConnectionIds().forEach(cid -> sender.send(what, cid));
		}

	}