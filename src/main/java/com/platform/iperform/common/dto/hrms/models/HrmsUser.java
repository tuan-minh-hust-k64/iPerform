package com.platform.iperform.common.dto.hrms.models;

import lombok.Data;

import java.util.List;

@Data
public class HrmsUser extends BaseHrmsModel{
    private String email;
    private String avatar;
    private String name;
    private String id_employee;
    private Boolean is_active;
    private String start_date;
    private String end_date;
    private List<HrmsManagerTeams> manager_teams;
    private HrmsLevel levels;
    private HrmsPosition positions;
    private HrmsRoleProject role_projects;
    private HrmsTeam teams;

}
