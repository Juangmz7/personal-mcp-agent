package com.juangomez.agentmcpserver.service;

public interface StorageService {

    void saveValue(String key, String value);

    boolean isKeySaved(String key);

    String getValue(String key);
}
