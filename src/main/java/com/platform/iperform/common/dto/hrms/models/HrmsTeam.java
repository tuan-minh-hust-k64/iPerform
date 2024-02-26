package com.platform.iperform.common.dto.hrms.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HrmsTeam team = (HrmsTeam) o;
        return id.equals(team.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
