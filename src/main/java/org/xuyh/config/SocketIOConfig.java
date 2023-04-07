/*
 * Copyright (c) 2020-2023 XuYanhang
 */

package org.xuyh.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * Configuration on EHCache. Use SocketIOServer in an injected way.
 *
 * @author XuYanhang
 * @since 2020-12-31
 */
@org.springframework.context.annotation.Configuration
public class SocketIOConfig {
    @Value("${websocket.server.ip}")
    private String ip;

    @Value("${websocket.server.port}")
    private int port;

    /**
     * New instance from Spring Boot
     */
    public SocketIOConfig() {
        super();
    }

    /**
     * Create SocketIOServer configuration into spring context. Use autowired inject
     * operation of type SocketIOServer.
     *
     * @return a SocketIOServer
     */
    @Bean
    @Scope("singleton")
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(ip);
        config.setPort(port);
        return new SocketIOServer(config);
    }

    @Bean
    @Scope("singleton")
    public SpringAnnotationScanner socketIOServerScanner(SocketIOServer socketIOServer) {
        return new SpringAnnotationScanner(socketIOServer);
    }

    @ConditionalOnProperty("websocket.server.enable")
    @Bean
    @Scope("singleton")
    public SocketIOServerCommandLineRunner socketIOServerRunner(SocketIOServer socketIOServer) {
        return new SocketIOServerCommandLineRunner(socketIOServer);
    }

    static class SocketIOServerCommandLineRunner implements CommandLineRunner {
        SocketIOServer socketIOServer;

        /**
         * @param server create server param
         */
        public SocketIOServerCommandLineRunner(SocketIOServer server) {
            super();
            this.socketIOServer = server;
        }

        @Override
        public void run(String... args) throws Exception {
            socketIOServer.start();
        }

        @javax.annotation.PreDestroy
        public void shutdown() {
            socketIOServer.stop();
        }
    }
}
