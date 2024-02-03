package com.platform.iperform.common.utils;

import com.google.gson.reflect.TypeToken;
import com.platform.iperform.common.dto.graphql.ManagerTeamType;
import com.platform.iperform.common.dto.request.AuthRequest;
import com.platform.iperform.common.exception.AuthenticateException;
import com.platform.iperform.model.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Component
@Slf4j
public class FunctionHelper {
    private final static Gson GSON = new Gson();
    private final static WebClient webClient = WebClient.builder()
            .baseUrl("https://users.ikameglobal.com/graphql")
            .build();
    private final static HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder(webClient)
            .headers(headers -> {
                headers.add("apikey", "zl78vy2caj1vhmhwzf5qaf36h5p00z");
                headers.add("passport", "minhvt@2024");
            })
            .build();
    public String calculateQuarter() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int quarter = (int) Math.ceil(month / 6.0);
        return "H" + quarter + "-" + year;
    }


    public String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            if (src.getPropertyValue(pd.getName()) == null || Objects.requireNonNull(src.getPropertyValue(pd.getName())).toString().isEmpty()) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public ZonedDateTime getZoneDateTime(ZonedDateTime source) {
        if(source == null) return ZonedDateTime.now(ZoneId.of("UTC"));
        return source;
    }

    public List<Object> getAllUserFromHrm() {
        String query = "query{\n" +
                "  getAllUsers{\n" +
                "    id\n" +
                "    email\n" +
                "    avatar\n" +
                "    name\n" +
                "    id_employee\n" +
                "    is_active\n" +
                "    start_date\n" +
                "    positions{\n" +
                "      name\n" +
                "    }\n" +
                "    teams{\n" +
                "      name\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

        Mono<List<Object>> result = graphQlClient.document(query)
                .retrieve("getAllUsers").toEntityList(Object.class);
        return result.block();
    }
    public Object getMyTeamInfo(String userId) {
        String queryUserById = "query {\n" +
                "  \tgetUserById(id:\"" + userId + "\") {\n" +
                "    manager_teams{\n" +
                "      teams{\n" +
                "        id\n" +
                "        name\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        List<ManagerTeamType> result = graphQlClient.document(queryUserById)
                .retrieve("getUserById.manager_teams").toEntityList(ManagerTeamType.class).block();


        return result;
    }

    public Map<String, Object> authenticateHrm(AuthRequest authRequest) {
//        String googleAuth = "query {\n" +
//                "\tgoogleAuth(auth: {\n" +
//                "    code: \"" + authRequest.getCode() + "\", \n" +
//                "    resource_type: \"" + authRequest.getResourceType() + "\"\n" +
//                "  }) {\n" +
//                "    access {\n" +
//                "      resource\n" +
//                "      permission\n" +
//                "    }\n" +
//                "    name\n" +
//                "    id_token\n" +
//                "    userpfp\n" +
//                "    position_id\n" +
//                "    username\n" +
//                "    useremail\n" +
//                "    user_id\n" +
//                "    team_id\n" +
//                "    team_name\n" +
//                "    position_name\n" +
//                "  }\n" +
//                "}";
//        return graphQlClient.document(googleAuth)
//                .retrieve("googleAuth").toEntity(Object.class).block();


        String jsonBody = "{ \"code\": \"" + authRequest.getCode() + "\","+ "\"resource_type\": \"" + "iPerform" + "\"}";
        HttpURLConnection conn = null;
        try {
            conn = postHttpURLConnection(
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

    public List<Permission> authorizationHrm(AuthRequest authRequest) throws IOException {
        StringBuilder url = new StringBuilder("https://hrms-dev.ikamegroup.com/api/v1/iam/users/get-list-access");
        url.append("?user_id=").append(URLEncoder.encode(authRequest.getUserId(), StandardCharsets.UTF_8))
                .append("&resource_type=").append(URLEncoder.encode(authRequest.getResourceType(), StandardCharsets.UTF_8));
        HttpURLConnection conn = getHttpURLConnection(url.toString());
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

    public Map<String, Object> getManagerInfo(String userId) throws IOException {
        StringBuilder url = new StringBuilder("https://hrms-dev.ikamegroup.com/api/v1/iam/users/get-manager-of-user");
        url.append("?user_id=").append(URLEncoder.encode(userId, StandardCharsets.UTF_8));
        HttpURLConnection conn = getHttpURLConnection(url.toString());
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
    public List<Map<String, Object>> getTeamByManagerId(String userId) throws IOException {
        StringBuilder url = new StringBuilder("https://hrms-dev.ikamegroup.com/api/v1/iam/users/get-all-for-team-permissions");
        url.append("?user_id=").append(URLEncoder.encode(userId, StandardCharsets.UTF_8));
        HttpURLConnection conn = getHttpURLConnection(url.toString());
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

    public UUID authorizationMiddleware(UUID idManager, UUID idUser) {
        if(idManager == idUser) return idManager;
        if(checkPermissionHrm(idManager, idUser)) {
            return idUser;
        } else {
            throw new AuthenticateException("You not permission!");
        }
    }

    public boolean checkPermissionHrm(UUID idManager, UUID idUser) {
        if(idManager.equals(idUser)) return true;
        String jsonBody = "{\"user_member\": \"" + idUser + "\",\"user_manager\": \"" + idManager + "\"}";
        HttpURLConnection conn = null;
        Map<String, String> requestProperty = new HashMap<>();
        requestProperty.put("apikey", "dcXgdwbqU9sgZ6rGjWr8yAGXjGvHOgbi");
        try {
            conn = postHttpURLConnection(
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

    public String generateEmailRequestCheckIn(String content, UUID userId) {
        return "";
    }

    private static HttpURLConnection postHttpURLConnection(String linkUrl, String jsonBody,
                                                           Map<String, String> requestProperty
    ) throws IOException {
        URL url = new URL(linkUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        requestProperty.keySet().forEach(item -> {
            conn.setRequestProperty(item, requestProperty.get(item));
        });
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
        conn.setRequestProperty("apikey", "dcXgdwbqU9sgZ6rGjWr8yAGXjGvHOgbi");
        conn.setDoOutput(true);
        return conn;
    }
}
