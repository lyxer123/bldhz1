package com.bld.project.newlyadded.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
//@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "dev")
@Component
public class WebSocketConfig {


    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
