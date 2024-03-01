package com.platform.iperform.common.dto.hrms.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HrmsManagerInfo {
    private String id;
    private String email;
    @JsonProperty("team_id")
    private String teamId;
    @JsonProperty("position_id")
    private String positionId;
    private String name;
}
