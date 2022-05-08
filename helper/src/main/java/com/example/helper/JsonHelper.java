package com.example.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

    private static ObjectMapper jsonMapper = new ObjectMapper();

    public static String writeAsString(Object value) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(value);
    }

    public static byte[] writeAsBytes(Object value) throws JsonProcessingException {
        return jsonMapper.writeValueAsBytes(value);
    }

    public static <T> T readFromString(String json, Class<T> clazz) throws JsonProcessingException {
        return jsonMapper.readValue(json, clazz);
    }

}
