package com.ohgiraffers.semiproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 브로커를 통한 메시지 전달 경로
        config.setApplicationDestinationPrefixes("/app"); // 애플리케이션 목적지 접두사
    }

    // STOMP 엔드포인트 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat") // 웹소켓 엔드포인트
                .setAllowedOrigins("http://localhost:9595") // 정확한 Origin 설정
                .withSockJS(); // SockJS 사용
    }
}
