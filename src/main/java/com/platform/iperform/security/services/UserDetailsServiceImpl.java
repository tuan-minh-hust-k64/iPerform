package com.platform.iperform.security.services;

import com.platform.iperform.common.dto.request.AuthRequest;
import com.platform.iperform.common.utils.FunctionHelper;
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
  private final FunctionHelper functionHelper;

  public UserDetailsServiceImpl(FunctionHelper functionHelper) {
    this.functionHelper = functionHelper;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      List<Permission> permission = functionHelper.authorizationHrm(AuthRequest.builder()
                      .userId(username)
                      .resourceType("iPerform")
              .build());
      User user = User.builder()
              .id(UUID.randomUUID())
              .username(username)
              .roles(permission.stream().map(item -> "ROLE_" + item.permission().toUpperCase()).distinct().toList())
              .password(new BCryptPasswordEncoder().encode(username))
              .build();

      return UserDetailsImpl.build(user);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
