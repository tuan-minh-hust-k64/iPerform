package com.platform.iperform.common.dto.hrms.models;

import lombok.Data;

@Data
public class HrmsApiKey extends BaseHrmsModel {
    private String value;
    private HrmsPermission permissions;
    private HrmsResource resources;
    private HrmsUser users;

}
