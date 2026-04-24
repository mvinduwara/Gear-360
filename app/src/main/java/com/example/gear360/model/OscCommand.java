package com.example.gear360.model;

import java.util.HashMap;
import java.util.Map;

public class OscCommand {
    private String name;
    private Map<String, Object> parameters;

    public OscCommand(String name) {
        this.name = name;
        this.parameters = new HashMap<>();
    }

    public void addParameter(String key, Object value) {
        this.parameters.put(key, value);
    }
}