package com.etest;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PayloadUtil {

    static File setFilePath(String env, String json) {
        String dir = env.equalsIgnoreCase("staging")
                ? "/stagingData/"+json+".json" : env.equalsIgnoreCase("production")
                ? "/productionData/"+json+".json" : null;
        return new File(System.getProperty("user.dir"), "src/test" + dir);
    }

    public static String generateRandomTitle(int wordCount, String env) {
        ArrayList<String> words = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            words = objectMapper.readValue(setFilePath(env, "wordBank"), new TypeReference<ArrayList<String>>(){});
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        int x = words.size();
        StringBuilder newWord = new StringBuilder();
        for(int i = 0; wordCount > i; i++) {
            int a = (int) Math.round(Math.random() * x);
            newWord.append(words.get(a) + (wordCount > i+1 ? " ":"."));
        }
       return newWord.toString();
    }
    public static Map<String, Object> readPayloadFromFile(String env) {
        Map<String, Object> payload = null;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            payload = objectMapper.readValue(setFilePath(env, "data"), new TypeReference<Map<String, Object>>(){});
        }catch (Exception je) {
            je.printStackTrace();
        }
        return payload;
    }

    public static List<Map<String, Object>> readPayloadFromResponseAsList(String response) {
        List<Map<String, Object>> payload = null;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            payload = objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>(){});
        }catch (JacksonException je) {
            je.printStackTrace();
        }
        return payload;
    }

    public static Map<String, Object> readPayloadFromResponseAsObject(String response) {
        Map<String, Object> payload = null;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            payload = objectMapper.readValue(response, new TypeReference<Map<String, Object>>(){});
        }catch (Exception je) {
            je.printStackTrace();
        }
        return payload;
    }
    public static void saveNewJsonData(Map<String, Object> payload, String env) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(setFilePath(env,"data"), payload);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
