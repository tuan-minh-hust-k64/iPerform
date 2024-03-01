package com.platform.iperform.common.dto.hrms.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class HrmsUser extends BaseHrmsModel{
    private String id;
    private String email;
    private String avatar;
    private String name;
    @JsonProperty("id_employee")
    private String idEmployee;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("is_partner")
    private Boolean isPartner;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    @JsonProperty("manager_teams")
    private List<HrmsManagerTeams> managerTeams;
    private HrmsLevel levels;
    private HrmsPosition positions;
    @JsonProperty("role_projects")
    private HrmsRoleProject roleProjects;
    private HrmsTeam teams;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HrmsUser user = (HrmsUser) o;
        return id.equals(user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
