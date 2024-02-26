package com.platform.iperform.libs.hrms_provider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.iperform.common.dto.hrms.models.HrmsAccess;
import com.platform.iperform.common.dto.hrms.models.HrmsUser;
import com.platform.iperform.common.dto.hrms.response.HrmsLoginResponse;
import com.platform.iperform.common.dto.request.AuthRequest;
import com.platform.iperform.common.exception.AuthenticateException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.model.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class HrmsV2 implements HrmsProvider{
    private final FunctionHelper functionHelper;
    private final static Gson GSON = new Gson();

    HrmsV2(FunctionHelper functionHelper) {
        this.functionHelper = functionHelper;
    }
    @Override
    public HrmsLoginResponse authenticateHrm(AuthRequest authRequest) {
        String jsonBody = "{ \"code\": \"" + authRequest.getCode() + "\","+ "\"resource_type\": \"" + "iPerform" + "\"}";
        HttpURLConnection conn = null;
        try {
            conn = functionHelper.postHttpURLConnection(
                    "https://hrms-dev.ikamegroup.com/auth/login/google-auth",
                    jsonBody,
                    new HashMap<>()
            );
        } catch (IOException e) {
            throw new AuthenticateException("Could not authenticate in HRM");
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return GSON.fromJson(response.toString(), new TypeToken<Map<String, ?>>(){}.getType());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new AuthenticateException("Could not load authenticate in HRM");
        }
    }

    @Override
    public List<HrmsAccess> authorizationHrm(AuthRequest authRequest) throws Exception {
        StringBuilder url = new StringBuilder("https://hrms-dev.ikamegroup.com/api/v1/iam/users/get-list-access");
        url.append("?user_id=").append(URLEncoder.encode(authRequest.getUserId(), StandardCharsets.UTF_8))
                .append("&resource_type=").append(URLEncoder.encode(authRequest.getResourceType(), StandardCharsets.UTF_8));
        HttpURLConnection conn = functionHelper.getHttpURLConnection(url.toString());
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return GSON.fromJson(response.toString(), new TypeToken<List<Permission>>(){}.getType());
        }
    }

    @Override
    public Map<String, Object> getManagerInfo(String userId) throws Exception {
        StringBuilder url = new StringBuilder("https://hrms-dev.ikamegroup.com/api/v1/iam/users/get-manager-of-user");
        url.append("?user_id=").append(URLEncoder.encode(userId, StandardCharsets.UTF_8));
        HttpURLConnection conn = functionHelper.getHttpURLConnection(url.toString());
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return GSON.fromJson(response.toString(), new TypeToken<Map<String, ?>>(){}.getType());
        }
    }

    @Override
    public List<HrmsUser> getTeamByManagerId(String userId) throws Exception {
        StringBuilder url = new StringBuilder("https://hrms-dev.ikamegroup.com/api/v1/iam/users/get-all-for-team-permissions");
        url.append("?user_id=").append(URLEncoder.encode(userId, StandardCharsets.UTF_8));
        HttpURLConnection conn = functionHelper.getHttpURLConnection(url.toString());
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return GSON.fromJson(response.toString(), new TypeToken<List<Map<String, ?>>>(){}.getType());
        }
    }

    @Override
    public boolean checkPermissionHrm(UUID idManager, UUID idUser) {
        if(idManager.equals(idUser)) return true;
        String jsonBody = "{\"user_member\": \"" + idUser + "\",\"user_manager\": \"" + idManager + "\"}";
        HttpURLConnection conn = null;
        Map<String, String> requestProperty = new HashMap<>();
        requestProperty.put("apikey", "dcXgdwbqU9sgZ6rGjWr8yAGXjGvHOgbi");
        try {
            conn = functionHelper.postHttpURLConnection(
                    "https://hrms-dev.ikamegroup.com/api/v1/iam/users/check-user-permissions",
                    jsonBody, requestProperty);
        } catch (IOException e) {
            throw new AuthenticateException("Could not authenticate in HRM");
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            Map<String, Object> result = GSON.fromJson(response.toString(), new TypeToken<Map<String, ?>>(){}.getType());

            return (boolean) result.get("is_manager_of_user_member");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new AuthenticateException("Could not load authenticate permission in HRM");
        }
    }

    @Override
    public List<HrmsUser> getAllUsers() {
        return null;
    }
}
