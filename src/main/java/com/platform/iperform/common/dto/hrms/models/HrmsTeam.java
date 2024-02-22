package com.platform.iperform.common.dto.hrms.models;

import lombok.Data;

import java.util.List;

@Data
public class HrmsTeam extends BaseHrmsModel{
    private String name;
    private String full_name;
    private String label;
    private Boolean is_active;
    private HrmsManagerTeams manager_teams;
    private HrmsTeam parent_teams;
    private List<HrmsTeam> sub_teams;
    private List<HrmsUser> users;
}
