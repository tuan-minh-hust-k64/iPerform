package com.platform.iperform.common.dto.hrms.models;

import lombok.Data;

import java.util.List;

@Data
public class HrmsManagerTeams extends BaseHrmsModel{
    private List<HrmsUser> users_type;
    private List<HrmsTeam> teams;
}
