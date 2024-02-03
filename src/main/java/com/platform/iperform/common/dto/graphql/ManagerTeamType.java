package com.platform.iperform.common.dto.graphql;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ManagerTeamType {
    private final String id;
    private final TeamType teams;
}
