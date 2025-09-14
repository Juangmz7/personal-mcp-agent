package com.juangomez.agentmcpserver.service;

import com.juangomez.agentmcpserver.dto.EmailRequest;
import com.juangomez.agentmcpserver.dto.SetEmailCredentialsRequest;
import com.juangomez.agentmcpserver.dto.ToolResult;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public interface MailService {

    ToolResult setEmailCredentials(@Valid SetEmailCredentialsRequest setPasswordRequest);

    ToolResult sendEmail(@Valid EmailRequest emailRequest);

}
