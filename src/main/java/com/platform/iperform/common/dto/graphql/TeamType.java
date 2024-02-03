package com.platform.iperform.common.dto.graphql;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TeamType {
    private final String id;
    private final String name;
    private final String full_name;
    private final boolean is_active;
}
