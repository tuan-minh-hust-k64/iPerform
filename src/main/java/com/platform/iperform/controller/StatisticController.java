package com.platform.iperform.controller;

import com.platform.iperform.common.dto.hrms.models.HrmsRoleProject;
import com.platform.iperform.common.dto.hrms.models.HrmsTeam;
import com.platform.iperform.common.dto.hrms.models.HrmsUser;
import com.platform.iperform.common.dto.request.CheckPointRequest;
import com.platform.iperform.common.dto.response.CheckPointResponse;
import com.platform.iperform.common.dto.response.CollaborationFeedbackResponse;
import com.platform.iperform.common.dto.response.EksResponse;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.common.valueobject.CheckInStatus;
import com.platform.iperform.common.valueobject.FeedbackStatus;
import com.platform.iperform.libs.hrms_provider.HrmsProvider;
import com.platform.iperform.libs.hrms_provider.HrmsV3;
import com.platform.iperform.model.CheckPoint;
import com.platform.iperform.model.CollaborationFeedback;
import com.platform.iperform.service.CheckPointService;
import com.platform.iperform.service.CollaborationFeedbackService;
import com.platform.iperform.service.EksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/statistic")
@CrossOrigin(origins = {"http://localhost:3000", "https://iperform.ikameglobal.com"}, allowCredentials = "true")
@Slf4j
public class StatisticController {
    private final FunctionHelper functionHelper;
    private final CheckPointService checkPointService;
    private final CollaborationFeedbackService collaborationFeedbackService;
    private final EksService eksService;
    private final HrmsProvider hrmsProvider;

    public StatisticController(FunctionHelper functionHelper,
                               CheckPointService checkPointService,
                               CollaborationFeedbackService collaborationFeedbackService,
                               EksService eksService,
                               HrmsV3 hrmsProvider
    ) {
        this.functionHelper = functionHelper;
        this.checkPointService = checkPointService;
        this.collaborationFeedbackService = collaborationFeedbackService;
        this.eksService = eksService;
        this.hrmsProvider = hrmsProvider;
    }

    @GetMapping(value = "/my-team")
    public ResponseEntity<?> statisticMyTeam(@RequestParam String managerId, @RequestParam String title, @RequestParam(required = false) String category) {
        try {
//            List<Map<String, Object>> result = functionHelper.getTeamByManagerId(managerId);
            List<HrmsUser> listUser = hrmsProvider.getTeamByManagerId(managerId);

            List<Map<String, Object>> result = new ArrayList<>();

                listUser.forEach(item -> {
                EksResponse statisticEks = eksService.getEksByUserId(UUID.fromString(item.getId()), title, category);
                AtomicReference<String> checkInStatus = new AtomicReference<>("COMPLETED");

                Map<String, Object> user = new HashMap<>();
                user.put("id", item.getId());
                user.put("positions", item.getPositions());
                user.put("email", item.getEmail());
                user.put("role_projects", item.getRoleProjects());
                user.put("role_project_id", item.getRoleProjects() != null ? item.getRoleProjects().getId() : "");
                user.put("name", item.getName());
                user.put("team_id", item.getTeams() != null ? item.getTeams().getId() : "");
                user.put("teams", item.getTeams() != null ? item.getTeams() : "");
                user.put("avatar", item.getAvatar());
                user.put("start_date", item.getStartDate());

                for(int i = 0; i<statisticEks.getEks().size(); i++) {
                    if(!statisticEks.getEks().get(i).getCheckIns().stream().filter(temp -> temp.getStatus().equals(CheckInStatus.INIT)).toList().isEmpty()) {
                        checkInStatus.set("INIT");
                        break;
                    }
                    if(!statisticEks.getEks().get(i).getCheckIns().stream().filter(temp -> temp.getStatus().equals(CheckInStatus.PENDING)).toList().isEmpty()) {
                        checkInStatus.set("PENDING");
                        break;
                    }
                }
                CheckPointResponse statisticCheckPoint = checkPointService.findByUserIdAndTitle(CheckPointRequest.builder()
                        .userId(UUID.fromString(item.getId().toString()))
                                .title(title)
                        .build());
                CollaborationFeedbackResponse statisticFeedback = collaborationFeedbackService.getCollaborationByTargetIdAndTimePeriod(UUID.fromString(user.get("id").toString()), title, FeedbackStatus.INIT);
                user.put("checkPointStatus", statisticCheckPoint.getData().getStatus());
                user.put("checkPointId", statisticCheckPoint.getData().getId());
                user.put("ranking", statisticCheckPoint.getData().getRanking());
                user.put("feedBackStatus", statisticFeedback.getCollaborationFeedbacks().isEmpty());
                user.put("checkInStatus", checkInStatus);

                result.add(user);
            });
            return ResponseEntity.ok(result);

        } catch (IOException e) {
            log.error("Get Info Team By Manager Id Failure: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping(value = "/check-point")
    ResponseEntity<?> statisticCheckPoint(@RequestParam(required = false) String timePeriod) {
        try {
//            List<Map<String, Object>> result = functionHelper.getTeamByManagerId("2c7008db-1f20-4fbb-8d77-325431277220");
            List<HrmsUser> result = hrmsProvider.getTeamByManagerId("2c7008db-1f20-4fbb-8d77-325431277220");
            List<Map<String, Object>> data = result.stream().map(user -> {
                CheckPointResponse statisticCheckPoint = checkPointService.findByUserIdAndTitle(CheckPointRequest.builder()
                        .userId(UUID.fromString(user.getId()))
                        .title(timePeriod == null ? functionHelper.calculateQuarter():timePeriod)
                        .build());
                List<?> statisticFeedback = collaborationFeedbackService.getCollaborationByReviewerIdAndTimePeriod(
                        UUID.fromString(user.getId()),
                        timePeriod == null ? functionHelper.calculateQuarter() : timePeriod,
                        FeedbackStatus.INIT, FeedbackStatus.COMPLETED).getCollaborationFeedbacks().stream().map(x -> {
                            Map<String, String> tempFeedback = new HashMap<>();
                            tempFeedback.put("targetId", x.getTargetId().toString());
                            tempFeedback.put("status", x.getStatus().name());
                            return tempFeedback;
                }).toList();

//                Map<String, Object> tempRep = (Map<String, Object>) user.getTeams();
                HrmsTeam tempRep = user.getTeams();
                Map<String, Object> temp = new HashMap<>();
                Map<String, String> statisticCheckPointItem = new HashMap<>();
                statisticCheckPoint.getData().getCheckPointItems().forEach(checkPointItem -> {
                    if(!checkPointItem.getComments().isEmpty()) {
                        if(!Objects.equals(statisticCheckPointItem.get(checkPointItem.getTitle()), "COMPLETED")) {
                            statisticCheckPointItem.put(checkPointItem.getTitle(), "COMPLETED");
                        }
                    } else {
                        if(!Objects.equals(statisticCheckPointItem.get(checkPointItem.getTitle()), "COMPLETED")) {
                            statisticCheckPointItem.put(checkPointItem.getTitle(), "INIT");
                        }
                    }
                });
                temp.put("checkPointStatus", statisticCheckPoint.getData().getStatus());
                temp.put("checkPointItems", statisticCheckPointItem);
                temp.put("ranking", statisticCheckPoint.getData().getRanking());
                temp.put("id", user.getId());
                temp.put("feedbacks", statisticFeedback);
                temp.put("name", user.getName());
                temp.put("email", user.getEmail());
                temp.put("team_name",  tempRep != null ? tempRep.getFullName() : "");
                return temp;
            }).toList();
            Function<Map<String, Object>, String> classificationFunction = item -> item.get("team_name").toString();;
            Supplier<Map<String, List<Map<String, Object>>>> mapSupplier =  TreeMap::new;
            Map<String, List<Map<String, Object>>> groupedTeam = data.stream().collect(Collectors.groupingBy(classificationFunction, mapSupplier, Collectors.toList()));
            return ResponseEntity.ok(groupedTeam);

        } catch (IOException e) {
            log.error("Get Info Team By Manager Id Failure: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping(value = "/get-by-user-id")
    public ResponseEntity<?> getStatisticByUserId(@RequestParam String userId, @RequestParam(required = false) String timePeriod) {
        List<CheckPoint> dataCheckPoint = checkPointService.getCheckPointByUserId(CheckPointRequest.builder()
                .userId(UUID.fromString(userId))
                .build()).getCheckPoint().stream().filter(item -> item.getTitle().equals(timePeriod == null? functionHelper.calculateQuarter():timePeriod)).toList();

        List<CollaborationFeedback> dataFeedBack = collaborationFeedbackService.getCollaborationByTargetIdAndTimePeriod(
                UUID.fromString(userId), timePeriod, FeedbackStatus.INIT, FeedbackStatus.COMPLETED
        ).getCollaborationFeedbacks();
        Map<String, Object> data = new HashMap<>();
        data.put("checkPoints", dataCheckPoint);
        data.put("feedback", dataFeedBack);
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/eks")
    public ResponseEntity<?> statisticEks(@RequestParam(required = false) String timePeriod, @RequestParam(required = false) String category) {
//        List<Map<String, Object>> result = null;
        try {
//            result = functionHelper.getTeamByManagerId("2c7008db-1f20-4fbb-8d77-325431277220");
            List<HrmsUser> result = hrmsProvider.getTeamByManagerId("2c7008db-1f20-4fbb-8d77-325431277220");
            List<Map<String, Object>> data = result.stream().map(user -> {
                EksResponse statisticEks = eksService.getEksByUserId(UUID.fromString(user.getId()),
                        category == null ? null : timePeriod == null ? functionHelper.calculateQuarter() : timePeriod,
                        category) ;
//                Map<String, Object> tempRep = (Map<String, Object>) user.getTeams();
                HrmsTeam tempRep =  user.getTeams();
                Map<String, Object> temp = new HashMap<>();
                temp.put("id", user.getId());
                temp.put("name", user.getName());
                temp.put("email", user.getEmail());
                temp.put("team_name", tempRep !=null ? tempRep.getFullName() : "" );
                temp.put("eks", statisticEks.getEks());
                return temp;
            }).toList();
            Function<Map<String, Object>, String> classificationFunction = item -> item.get("team_name").toString();;
            Supplier<Map<String, List<Map<String, Object>>>> mapSupplier =  TreeMap::new;
            Map<String, List<Map<String, Object>>> groupedTeam = data.stream().collect(Collectors.groupingBy(classificationFunction, mapSupplier, Collectors.toList()));
            return ResponseEntity.ok(groupedTeam);
        } catch (IOException e) {
            log.error("Get Info Team By Manager Id Failure: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
