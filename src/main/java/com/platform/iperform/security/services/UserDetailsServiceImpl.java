package com.platform.iperform.security.services;

import com.platform.iperform.common.dto.hrms.models.HrmsAccess;
import com.platform.iperform.common.dto.request.AuthRequest;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.libs.hrms_provider.HrmsProvider;
import com.platform.iperform.libs.hrms_provider.HrmsV3;
import com.platform.iperform.model.Permission;
import com.platform.iperform.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
  private final HrmsProvider hrmsProvider;

  public UserDetailsServiceImpl(HrmsV3 hrmsProvider) {

    this.hrmsProvider = hrmsProvider;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
//      List<Permission> permission = functionHelper.authorizationHrm(AuthRequest.builder()
//                      .userId(username)
//                      .resourceType("iPerform")
//              .build());

      List<HrmsAccess> accessList = hrmsProvider.authorizationHrm(
              AuthRequest
                      .builder()
                      .userId(username)
                      .resourceType("iPerform")
                      .build());

      User user = User.builder()
              .id(UUID.randomUUID())
              .username(username)
              .roles(accessList.stream().map(item -> "ROLE_" + item.getPermission().toUpperCase()).distinct().toList())
              .password(new BCryptPasswordEncoder().encode(username))
              .build();

      return UserDetailsImpl.build(user);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
  }

}
