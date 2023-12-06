package com.platform.iperform.common.dto.request;

import com.platform.iperform.model.Config;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class ConfigRequest {
    private final UUID id;
    private final Config config;
}
