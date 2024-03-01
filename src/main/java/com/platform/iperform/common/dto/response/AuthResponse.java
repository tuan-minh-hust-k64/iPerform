package com.platform.iperform.common.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class AuthResponse {
    private final String user_id;
    private final List<String> access;
    private final Long issued_at;
    private final String username;
    private final String userpfp;
    private final String team_id;
    private final String team_name;
    private final String company_id;
    private final String company_name;


    @Override
    public String toString() {
        return "AuthResponse{" +
                "user_id='" + user_id + '\'' +
                ", access=" + access +
                ", issued_at=" + issued_at +
                ", username='" + username + '\'' +
                ", userpfp='" + userpfp + '\'' +
                ", team_id='" + team_id + '\'' +
                ", team_name='" + team_name + '\'' +
                ", company_id='" + company_id + '\'' +
                ", company_name='" + company_name + '\'' +
                '}';
    }
}
