package com.platform.iperform.common.dto.hrms.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class HrmsRoleProject extends BaseHrmsModel{
    private String name;
    @JsonProperty("role_permissions")
    private List<HrmsRolePermission> rolePermissions;
    private HrmsPosition positions;
    private List<HrmsUser> users;
}
