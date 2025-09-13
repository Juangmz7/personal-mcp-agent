package com.juangomez.agentmcpclient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatBotController {

    private final ChatClient chatClient;

    public ChatBotController(
            ChatClient.Builder chatClientBuilder,
            ToolCallbackProvider toolCallbackProvider
    ) {
        this.chatClient = chatClientBuilder
                .defaultSystem(
                        "Please prioritise context information for answering queries." +
                                "Give shorts, concise and to the point answers."
                )
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

    @GetMapping("/chat-bot")
    public String chatBot(@RequestParam String query) {
        return chatClient
                .prompt(query)
                .call()
                .content();
    }
}