package com.juangomez.agentmcpserver.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SetEmailCredentialsRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
