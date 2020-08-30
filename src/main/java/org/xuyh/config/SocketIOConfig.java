package org.xuyh.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class SocketIOConfig {

	@Value("${websocket.server.port}")
	private int port;

	/**
	 * 
	 */
	public SocketIOConfig() {
		super();
	}

	@Bean
	public SocketIOServer socketIOServer() {
		Configuration config = new Configuration();
		config.setHostname("0.0.0.0");
		config.setPort(port);
		return new SocketIOServer(config);
	}

	@Bean
	public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
		return new SpringAnnotationScanner(socketIOServer);
	}

}
