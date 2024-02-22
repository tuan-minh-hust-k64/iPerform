package com.platform.iperform.common.dto.hrms.models;

import lombok.Data;

@Data
public class HrmsResourceType extends BaseHrmsModel {
    private String name;
    private HrmsApiKey api_keys;
    private HrmsResource resources;
}
