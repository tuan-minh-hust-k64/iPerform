package com.platform.iperform.libs.hrms_provider;

import com.platform.iperform.common.dto.hrms.models.*;
import com.platform.iperform.common.dto.request.AuthRequest;
import com.platform.iperform.common.dto.hrms.response.HrmsLoginResponse;
import com.platform.iperform.common.exception.HrmsException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@Primary
public class HrmsV3 implements HrmsProvider{
    private final HttpGraphQlClient graphQlClient;

    public HrmsV3() {
        WebClient client = WebClient.builder()
//                .baseUrl("https://users.ikameglobal.com/graphql")
                .baseUrl("http://10.10.11.34:5003/graphql")
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
                                  is_manager
                                  start_date
                                }
                }""";

        try {

            return graphQlClient.
                    document(document).
                    variable("idToken", authRequest.getCode()).variable("resource_type", "iPerform").
                    retrieve("googleAuth").toEntity(HrmsLoginResponse.class).block();
        } catch (Exception exception) {
            throw new HrmsException("Error login: " + exception.getMessage());
        }
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
        try {
            HrmsUser user = graphQlClient
                    .document(document)
                    .variable("id", authRequest.getUserId())
                    .retrieve("getUserById")
                    .toEntity(HrmsUser.class)
                    .block();

            assert user != null;
            return getHrmsAccesses(user);
        } catch (Exception exception) {
            throw new HrmsException("Error user: " + exception.getMessage());
        }
    }

    @NotNull
    private static List<HrmsAccess> getHrmsAccesses(HrmsUser user) {
        List<HrmsRolePermission> rolePermissions = user.getRoleProjects().getRolePermissions();
        List<HrmsAccess> access = new ArrayList<>();

        rolePermissions.forEach(item -> {
            if (item.getResources().getResourceTypes()!=null
                    && item.getResources().getResourceTypes().getName().equals("iPerform")
            ) {
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
            manager.put("email", i.getUsers().getEmail());
            manager.put("team_id", user.getTeams()!=null ? user.getTeams().getId() : "");
            manager.put("position_id", user.getPositions() != null ? user.getPositions().getId() : "");

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

        List<String> teamIds = new ArrayList<>();

        user.getManagerTeams().forEach(team -> {
            if (team.getTeams() !=null) {
                teamIds.add(team.getTeams().getId());
            }
        });
        Set<HrmsTeam> listTeams = new HashSet<>();

        teamIds.forEach(id -> {
            try {
                List<HrmsTeam> teams = graphQlClient.
                        document(documentTeams).
                        variable("team_id", id).
                        retrieve("getAllChildTeams").toEntityList(HrmsTeam.class).block();

                if (teams != null) {
                    listTeams.addAll(teams);
                }
                ;
            } catch (Exception exception) {
                throw new HrmsException("Error get teams: " + exception.getMessage());
            }
        });

        Set<HrmsUser> usersSet = new HashSet<>();

        listTeams.forEach(team -> {
            List<HrmsUser> users = new ArrayList<>();

            if (team.getUsers() != null) {
                for (HrmsUser teamUser : team.getUsers()) {
//                  Filter not return current user and user is not partner
                    if (!teamUser.getId().equals(userId)) {
                        users.add(teamUser);
                    }
                }
            }
            usersSet.addAll(users);
        });

        return new ArrayList<>(usersSet);
    }

    @Override
    public boolean checkPermissionHrm(UUID idManager, UUID idUser) {
        if (idManager.equals(idUser)) {
            return true;
        }
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

        // Get all parent team of user's team
        try {
            List<HrmsTeam> teams = new ArrayList<>(Objects.requireNonNull(graphQlClient
                    .document(document)
                    .variable("team_id", user.getTeams().getId())
                    .retrieve("getAllParentTeams")
                    .toEntityList(HrmsTeam.class)
                    .block()));

            // Add user's team
            teams.add(user.getTeams());
            AtomicReference<Boolean> isManager = new AtomicReference<>(false);

            for (HrmsTeam team : teams) {
                for (HrmsManagerTeams managerTeam : team.getManagerTeams()) {
                    if (managerTeam.getId() != null && managerTeam.getUsers().getId().equals(idManager.toString())) {
                        isManager.set(true);
                        break;
                    }
                }
            }
            return isManager.get();
        } catch (Exception exception) {
            throw new HrmsException("Error get teams: " + exception.getMessage());
        }
    }

    @Override
    public List<HrmsUser> getAllUsers() throws Exception {
        try {
            String document = """
                    query getAllUsers {
                      getAllUsers {
                        id
                        created_at
                        updated_at
                        email
                        avatar
                        name
                        id_employee
                        is_active
                        start_date
                        end_date
                        positions {
                          name
                          id
                        }
                        role_projects {
                          name
                          id
                        }
                      }
                    }
                    """;
            return graphQlClient
                    .document(document)
                    .retrieve("getAllUsers")
                    .toEntityList(HrmsUser.class)
                    .block();
        } catch (Exception exception) {
            throw new HrmsException("Error get all user" + exception.getMessage());
        }
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
                      manager_teams {
                        id
                        teams {
                            id
                        }
                      }
                      positions {
                        id
                        name
                      }
                    }
                  }
                """;

        try {
            return graphQlClient.
                    document(document).
                    variable("id", userId).
                    retrieve("getUserById").toEntity(HrmsUser.class).block();
        } catch (Exception exception) {
            throw new HrmsException("Error get user width id " +userId + " :" + exception.getMessage());
        }
    }
}
