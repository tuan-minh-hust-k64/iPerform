package com.platform.iperform.common.dto.hrms.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class HrmsTeam extends BaseHrmsModel{
    private String id;
    private String name;
    @JsonProperty("full_name")
    private String fullName;
    private String label;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("manager_teams")
    private List<HrmsManagerTeams> managerTeams;
    @JsonProperty("parent_teams")
    private HrmsTeam parentTeams;
    @JsonProperty("sub_teams")
    private List<HrmsTeam> subTeams;
    private List<HrmsUser> users;
}
