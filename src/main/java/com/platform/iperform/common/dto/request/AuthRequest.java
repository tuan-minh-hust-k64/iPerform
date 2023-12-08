package com.platform.iperform.common.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthRequest {
    private final String code;
    private final String resourceType;
    private final String userId;

    @Override
    public String toString() {
        return "AuthRequest{" +
                "code='" + code + '\'' +
                ", resourceType='" + resourceType + '\'' +
                '}';
    }
}
