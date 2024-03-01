package com.platform.iperform.common.dto.hrms.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.iperform.common.dto.hrms.models.HrmsAccess;
import lombok.Data;

import java.util.List;

@Data
public class HrmsLoginResponse {
    @JsonProperty("user_id")
    private String userId;
    private List<HrmsAccess> access;
    @JsonProperty("issued_at")
    private Long issuedAt;
    private String username;
    @JsonProperty("useremail")
    private String userEmail;
    @JsonProperty("userpfp")
    private String userPfp;
    @JsonProperty("team_id")
    private String teamId;
    @JsonProperty("team_name")
    private String teamName;
    private String token;
    @JsonProperty("position_name")
    private String positionName;
    @JsonProperty("is_manager")
    private Boolean isManager;
    @JsonProperty("start_date")
    private String startDate;
}
