package com.platform.iperform.libs.hrms_provider;

import com.platform.iperform.common.dto.hrms.models.HrmsAccess;
import com.platform.iperform.common.dto.hrms.models.HrmsUser;
import com.platform.iperform.common.dto.request.AuthRequest;
import com.platform.iperform.common.dto.hrms.response.HrmsLoginResponse;
import com.platform.iperform.model.Permission;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface HrmsProvider {
    HrmsLoginResponse authenticateHrm(AuthRequest authRequest);
    List<HrmsAccess> authorizationHrm(AuthRequest authRequest) throws Exception;
    Map<String, Object> getManagerInfo(String userId) throws Exception;
    List<HrmsUser> getTeamByManagerId(String userId) throws Exception;
    boolean checkPermissionHrm(UUID idManager, UUID idUser);
    List<HrmsUser> getAllUsers() throws Exception;
}
