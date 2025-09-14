package com.juangomez.agentmcpserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ToolResult {
    private boolean success;
    private String message;
}
