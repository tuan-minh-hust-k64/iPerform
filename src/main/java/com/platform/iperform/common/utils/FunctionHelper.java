package com.platform.iperform.common.utils;

import com.google.gson.reflect.TypeToken;
import com.platform.iperform.common.dto.request.AuthRequest;
import com.platform.iperform.common.dto.response.AuthResponse;
import com.platform.iperform.model.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Component
@Slf4j
public class FunctionHelper {
    private final static Gson GSON = new Gson();

    public String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            if (src.getPropertyValue(pd.getName()) == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public ZonedDateTime getZoneDateTime(ZonedDateTime source) {
        if(source == null) return ZonedDateTime.now(ZoneId.of("UTC"));
        return source;
    }

    public AuthResponse authenticateHrm(AuthRequest authRequest) throws IOException {
        String jsonBody = "{ \"code\": \"" + authRequest.getCode() + "\","+ "\"resourceType\": \"" + authRequest.getResourceType() + "\"}";
        HttpURLConnection conn = postHttpURLConnection("http://10.10.11.12:8184/auth/fake", jsonBody);
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return GSON.fromJson(response.toString(), AuthResponse.class);
        }
    }

    public List<Permission> authorizationHrm(AuthRequest authRequest) throws IOException {
        String url = "https://hrms-dev.ikamegroup.com/api/v1/iam/users/get-list-access?user_id=53bf6a4f-e0be-4e60-8382-3e75d0664a84&resource_type=Web%20BOT";
        HttpURLConnection conn = getHttpURLConnection("https://hrms-dev.ikamegroup.com/api/v1/iam/users/get-list-access?user_id=53bf6a4f-e0be-4e60-8382-3e75d0664a84&resource_type=Web%20BOT");
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            log.info(response.toString());
            return GSON.fromJson(response.toString(), new TypeToken<List<Permission>>(){}.getType());
        }
    }

    private static HttpURLConnection postHttpURLConnection(String linkUrl, String jsonBody) throws IOException {
        URL url = new URL(linkUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return conn;
    }
    private static HttpURLConnection getHttpURLConnection(String linkUrl) throws IOException {
        URL url = new URL(linkUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        return conn;
    }
}
