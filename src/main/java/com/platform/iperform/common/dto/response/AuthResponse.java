package com.platform.iperform.common.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse {
    final String user_id;

    @Override
    public String toString() {
        return "AuthResponse{" +
                "user_id='" + user_id + '\'' +
                '}';
    }
}
