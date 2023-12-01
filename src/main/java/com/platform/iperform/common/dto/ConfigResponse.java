package com.platform.iperform.common.dto;

import com.platform.iperform.model.Config;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ConfigResponse {
    private final Config config;
    private final String message;
}
