package com.platform.iperform.common.dto;

import com.platform.iperform.model.Config;
import lombok.Builder;

import java.util.List;

@Builder
public class ConfigResponse {
    private final Config config;
    private final String message;
}
