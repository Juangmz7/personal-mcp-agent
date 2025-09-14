package com.juangomez.agentmcpserver.configuration;

import com.juangomez.agentmcpserver.service.MailService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ToolProviderConfig {

    @Bean
    public ToolCallbackProvider tools (MailService mailService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(mailService)
                .build();
    }

}
