package com.juangomez.agentmcpserver.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EmailRequest {

    @Email(message = "Send must have email format")
    @NotNull(message = "Sender email cannot be null")
    private String sender;

    @Email(message = "Receiver must have email format")
    @NotNull(message = "Receiver email cannot be null")
    private String receiver;

    @NotNull(message = "Message cannot be null")
    private String message;

    private String subject;

}
