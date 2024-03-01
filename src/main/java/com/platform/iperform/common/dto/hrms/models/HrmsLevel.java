package com.platform.iperform.common.dto.hrms.models;

import lombok.Data;

import java.util.List;

@Data
public class HrmsLevel extends BaseHrmsModel{
    private String name;
    private List<HrmsUser> users;
}
