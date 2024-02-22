package com.platform.iperform.libs.hrms_provider;

import com.platform.iperform.common.dto.hrms.models.HrmsAccess;
import com.platform.iperform.common.dto.hrms.models.HrmsRolePermission;
import com.platform.iperform.common.dto.hrms.models.HrmsUser;
import com.platform.iperform.common.dto.request.AuthRequest;
import com.platform.iperform.common.dto.hrms.response.HrmsLoginResponse;
import com.platform.iperform.model.Permission;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class HrmsV3 implements HrmsProvider{
    private final HttpGraphQlClient graphQlClient;

    public HrmsV3() {
        WebClient client = WebClient.builder()
                .baseUrl("https://users.ikameglobal.com/graphql")
                .defaultHeader("apikey", "a61aiz8dscq480lqgi5f1t5stmas54")
                .defaultHeader("passport", "quyennv@2024")
                .build();

        this.graphQlClient = HttpGraphQlClient.builder(client).build();
    }

    @Override
    public HrmsLoginResponse authenticateHrm(AuthRequest authRequest) {
        String document = """
                query Login($idToken: String!, $resource_type: String!) {
                     googleAuth(auth: { idToken: $idToken, resource_type: $resource_type }) {
                                  access {
                                    resource
                                    permission
                                  }
                                  issued_at
                                  access_token
                                  username
                                  userpfp
                                  useremail
                                  user_id
                                  team_id
                                  team_name
                                  position_name
                                }
                }""";

        HrmsLoginResponse res = graphQlClient.
                document(document).
                variable("idToken", authRequest.getCode()).variable("resource_type", "iPerform").
                retrieve("googleAuth").toEntity(HrmsLoginResponse.class).block();

        return res;
    }

    @Override
    public List<HrmsAccess> authorizationHrm(AuthRequest authRequest) throws Exception {
        String document = """
                 query getUserById($id: ID!) {
                    getUserById(id: $id) {
                      id
                      role_projects {
                        role_permissions {
                          resources {
                            name
                            resource_types {
                                name
                            }
                          }
                          permissions {
                            name
                          }
                        }
                      }
                    }
                  }
                """;
        HrmsUser user = graphQlClient
                .document(document)
                .variable("id", authRequest.getUserId())
                .retrieve("getUserById")
                .toEntity(HrmsUser.class)
                .block();

        List<HrmsAccess> access = getHrmsAccesses(user);

        log.info(access.toString());
        return access;
    }

    @NotNull
    private static List<HrmsAccess> getHrmsAccesses(HrmsUser user) {
        List<HrmsRolePermission> rolePermissions = user.getRole_projects().getRole_permissions();
        List<HrmsAccess> access = new ArrayList<>();

        rolePermissions.forEach(item -> {
            log.info(item.toString());
            if (item.getResources().getResourceTypes()!=null
                    && item.getResources().getResourceTypes().getName().equals("iPerform")
            ) {
                log.info(item.getResources().getResourceTypes().getName());
                access.add(HrmsAccess
                        .builder()
                        .resource(item.getResources().getName())
                        .permission(item.getPermissions().getName())
                        .build());

            }
        });
        return access;
    }

    @Override
    public Map<String, Object> getManagerInfo(String userId) {
        String document = """
                query Login($idToken: String!, $resource_type: String!) {
                     googleAuth(auth: { idToken: $idToken, resource_type: $resource_type }) {
                                  access {
                                    resource
                                    permission
                                  }
                                  issued_at
                                  access_token
                                  username
                                  userpfp
                                  useremail
                                  user_id
                                  team_id
                                  team_name
                                  position_name
                                }
                }""";

        HrmsLoginResponse res = graphQlClient.
                document(document).
                variable("id", userId).
                retrieve("googleAuth").toEntity(HrmsLoginResponse.class).block();

        return null;
    }

    @Override
    public List<Map<String, Object>> getTeamByManagerId(String userId) {
        return null;
    }

    @Override
    public boolean checkPermissionHrm(UUID idManager, UUID idUser) {
        return false;
    }
}
