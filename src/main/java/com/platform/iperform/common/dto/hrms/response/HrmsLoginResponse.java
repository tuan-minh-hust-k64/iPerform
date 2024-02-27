package com.platform.iperform.common.dto.hrms.response;

import com.platform.iperform.common.dto.hrms.models.HrmsAccess;
import lombok.Data;

import java.util.List;

@Data
public class HrmsLoginResponse {
    private String user_id;
    private List<HrmsAccess> access;
    private Long issued_at;
    private String username;
    private String useremail;
    private String userpfp;
    private String team_id;
    private String team_name;
    private String token;
    private String position_name;
    private Boolean is_manager;
}
