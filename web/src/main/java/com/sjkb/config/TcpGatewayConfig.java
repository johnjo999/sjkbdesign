package com.sjkb.config;

import com.sjkb.controller.Sender;
import com.sjkb.controller.TcpGatewayEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class TcpGatewayConfig {

	@Value("8443")
	private int port;

	@Autowired
	TcpGatewayEndpoint tcpGatewayEndpoint;

	@MessagingGateway(defaultRequestChannel = "toTcp")
	public interface Gateway {

		String viaTcp(String in);

	}

	@Bean
	public MessageChannel fromTcp() {
		return new DirectChannel();
	}

	@Bean
	public AbstractServerConnectionFactory serverCF() {
		return new TcpNetServerConnectionFactory(this.port);
	}

	/*
	 * Inbound adapter - sends "connected!".
	 */
	@Bean
	public IntegrationFlow tcpServer(AbstractServerConnectionFactory serverFactory) {
		IntegrationFlowBuilder flow = IntegrationFlows.from(Tcp.inboundAdapter(serverFactory));
		flow.transform(p -> tcpGatewayEndpoint.convert((byte[]) p));
		flow.channel(tcpGatewayEndpoint.channel());
		IntegrationFlow result = flow.get();
		tcpGatewayEndpoint.stashChannel(result);
		return result;
	}

	/*
	 * Gateway flow for controller.
	 */
	@Bean
	public IntegrationFlow gateway() {
		return IntegrationFlows.from(Sender.class).channel("toTcp.input").get();
	}

	/*
	 * Outbound channel adapter flow.
	 */
	@Bean
	public IntegrationFlow toTcp(AbstractServerConnectionFactory serverFactory) {
		return f -> f.handle(Tcp.outboundAdapter(serverFactory));
	}

	/*
	 * Excutor for clients.
	 */
	@Bean
	public ThreadPoolTaskExecutor exec() {
		ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
		exec.setCorePoolSize(5);
		return exec;
	}

	

}
