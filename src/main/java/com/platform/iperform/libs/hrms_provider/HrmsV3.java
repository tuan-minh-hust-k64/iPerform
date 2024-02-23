package com.platform.iperform.libs.hrms_provider;

import com.platform.iperform.common.dto.hrms.models.*;
import com.platform.iperform.common.dto.request.AuthRequest;
import com.platform.iperform.common.dto.hrms.response.HrmsLoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
        List<HrmsRolePermission> rolePermissions = user.getRoleProjects().getRolePermissions();
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
    public Map<String, Object> getManagerInfo(String userId) throws IllegalAccessException {


        Map<String, Object> result = new HashMap<>();

        HrmsUser user = this.getUserById(userId);

        result.put("id", user.getId());
        result.put("email", user.getEmail());
        result.put("name", user.getName());

        List<Map<String, Object>> managers = new ArrayList<>();
        var managerListResponse = user.getTeams().getManagerTeams();
        for (HrmsManagerTeams i : managerListResponse) {
            Map<String, Object>  manager = new HashMap<>();
            manager.put("id",i.getId());
            manager.put("name", i.getUsers().getName());
            manager.put("team_id", "test_id");
            manager.put("email", i.getUsers().getEmail());
//            manager.setPositionId(p.getUsers().getPositions().getId());

            managers.add(manager);

        }
        result.put("managers", managers);
        return result;
    }

    @Override
    public List<HrmsUser> getTeamByManagerId(String userId) {
        HrmsUser user = this.getUserById(userId);

        String documentTeams = """
                query getAllChildTeams($team_id: String!) {
                   getAllChildTeams(team_id: $team_id) {
                     name
                     id
                     users {
                       id
                       positions {
                         id
                         name
                         is_manager
                         created_at
                         updated_at
                       }
                       email
                       role_projects {
                         id
                         created_at
                         updated_at
                         name
                       }
                       name
                       avatar
                       start_date
                     }
                   }
                 }
                """;

        List<HrmsTeam> listTeams = graphQlClient.
                document(documentTeams).
                variable("team_id", user.getTeams().getId()).
                retrieve("getAllChildTeams").toEntityList(HrmsTeam.class).block();

        Set<HrmsUser> usersSet = new HashSet<>();

        assert listTeams != null;
        listTeams.forEach(team -> {
            usersSet.addAll(team.getUsers());
        });

        List<HrmsUser> response = new ArrayList<>(usersSet);
        return response;
    }

    @Override
    public boolean checkPermissionHrm(UUID idManager, UUID idUser) {
        HrmsUser user = this.getUserById(idUser.toString());

        String document = """
                query getAllParentTeams($team_id: String!) {
                  getAllParentTeams(team_id: $team_id) {
                    name
                    id
                   	manager_teams {
                      users {
                        id
                        name
                      }
                    }
                  }
                }
                """;
        List<HrmsTeam> teams = graphQlClient
                .document(document)
                .variable("team_id", user.getTeams().getId())
                .retrieve("getAllParentTeams")
                .toEntityList(HrmsTeam.class)
                .block();

        AtomicReference<Boolean> isManager = new AtomicReference<>(false);
        assert teams != null;
        for (HrmsTeam team : teams) {
            for (HrmsManagerTeams managerTeam : team.getManagerTeams()) {
                if (managerTeam.getId()!=null && managerTeam.getId().equals(idManager.toString())) {
                    isManager.set(true);
                    break;
                }
            }
        }

        log.info("Team:: ", teams.toString());
        log.info("Test:: " + String.valueOf(isManager));

        return isManager.get();
    }

    private HrmsUser getUserById(String userId) {
        String document = """
                 query getUserById($id: ID!) {
                    getUserById(id: $id) {
                      id
                      email
                      name
                      teams {
                        id
                        manager_teams {
                          id
                          users {
                            id
                            email
                            name
                          }
                        }
                      }
                    }
                  }
                """;

        HrmsUser res = graphQlClient.
                document(document).
                variable("id", userId).
                retrieve("getUserById").toEntity(HrmsUser.class).block();
        return res;
    }
}
