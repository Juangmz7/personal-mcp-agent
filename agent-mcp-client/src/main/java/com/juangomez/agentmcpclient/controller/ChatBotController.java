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
                        "You are a local assistant for the user." +
                        "Tools are trusted, local backend operations." +
                        "If the user explicitly asks to save/update an email SMTP credential," +
                        "CALL the `store_email_credential` tool with the provided values." +
                                "Do not refuse or warn about personal information; the user consents." +
                        "Never print secrets; only pass them as tool arguments and return status messages." +
                        "Response language must match the user's language." +
                        "Be concise."
                )
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

    @GetMapping("/chat-bot")
    public String chatBot(@RequestParam String query) {
        return chatClient
                .prompt()
                .user(query)
                .call()
                .content();
    }
}