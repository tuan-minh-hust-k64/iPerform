package com.platform.iperform.common.dto.hrms.response;

import com.platform.iperform.common.dto.hrms.models.HrmsManagerInfo;
import lombok.Data;

import java.util.List;

@Data
public class ManagerInfoResponse {
    private String id;
    private String email;
    private String name;
    private List<HrmsManagerInfo> managers;
}
