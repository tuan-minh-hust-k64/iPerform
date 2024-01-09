package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.CheckPointRequest;
import com.platform.iperform.common.dto.response.CheckPointResponse;
import com.platform.iperform.common.dto.response.CollaborationFeedbackResponse;
import com.platform.iperform.common.dto.response.EksResponse;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.common.valueobject.CheckInStatus;
import com.platform.iperform.common.valueobject.FeedbackStatus;
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

    public StatisticController(FunctionHelper functionHelper,
                               CheckPointService checkPointService,
                               CollaborationFeedbackService collaborationFeedbackService,
                               EksService eksService) {
        this.functionHelper = functionHelper;
        this.checkPointService = checkPointService;
        this.collaborationFeedbackService = collaborationFeedbackService;
        this.eksService = eksService;
    }

    @GetMapping(value = "/my-team")
    public ResponseEntity<?> statisticMyTeam(@RequestParam String managerId, @RequestParam String title) {
        try {
            List<Map<String, Object>> result = functionHelper.getTeamByManagerId(managerId);
            result.forEach(item -> {
                EksResponse statisticEks = eksService.getEksByUserId(UUID.fromString(item.get("id").toString()), title);
                AtomicReference<String> checkInStatus = new AtomicReference<>("COMPLETED");
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
                        .userId(UUID.fromString(item.get("id").toString()))
                                .title(title)
                        .build());
                CollaborationFeedbackResponse statisticFeedback = collaborationFeedbackService.getCollaborationByTargetIdAndTimePeriod(UUID.fromString(item.get("id").toString()), title, FeedbackStatus.INIT);
                item.put("checkPointStatus", statisticCheckPoint.getData().getStatus());
                item.put("checkPointId", statisticCheckPoint.getData().getId());
                item.put("ranking", statisticCheckPoint.getData().getRanking());
                item.put("feedBackStatus", statisticFeedback.getCollaborationFeedbacks().isEmpty());
                item.put("checkInStatus", checkInStatus);
            });
            return ResponseEntity.ok(result);

        } catch (IOException e) {
            log.error("Get Info Team By Manager Id Failure: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @GetMapping(value = "/check-point")
    ResponseEntity<?> statisticCheckPoint(@RequestParam(required = false) String timePeriod) {
        try {
            List<Map<String, Object>> result = functionHelper.getTeamByManagerId("2c7008db-1f20-4fbb-8d77-325431277220");
            List<Map<String, Object>> data = result.stream().map(item -> {
                CheckPointResponse statisticCheckPoint = checkPointService.findByUserIdAndTitle(CheckPointRequest.builder()
                        .userId(UUID.fromString(item.get("id").toString()))
                        .title(timePeriod == null ? functionHelper.calculateQuarter():timePeriod)
                        .build());
                List<?> statisticFeedback = collaborationFeedbackService.getCollaborationByReviewerIdAndTimePeriod(UUID.fromString(item.get("id").toString()),
                        timePeriod == null ? functionHelper.calculateQuarter() : timePeriod,
                        FeedbackStatus.INIT, FeedbackStatus.COMPLETED).getCollaborationFeedbacks().stream().map(x -> {
                            Map<String, String> tempFeedback = new HashMap<>();
                            tempFeedback.put("targetId", x.getTargetId().toString());
                            tempFeedback.put("status", x.getStatus().name());
                            return tempFeedback;
                }).toList();

                Map<String, Object> tempRep = (Map<String, Object>) item.get("teams");
                Map<String, Object> temp = new HashMap<>();
                temp.put("checkPointStatus", statisticCheckPoint.getData().getStatus());
                temp.put("ranking", statisticCheckPoint.getData().getRanking());
                temp.put("id", item.get("id"));
                temp.put("feedbacks", statisticFeedback);
                temp.put("name", item.get("name"));
                temp.put("email", item.get("email"));
                temp.put("team_name", tempRep.get("full_name"));
                return temp;
            }).toList();
            Function<Map<String, Object>, String> classificationFunction = item -> item.get("team_name").toString();;
            Supplier<Map<String, List<Map<String, Object>>>> mapSupplier =  TreeMap::new;
            Map<String, List<Map<String, Object>>> groupedTeam = data.stream().collect(Collectors.groupingBy(classificationFunction, mapSupplier, Collectors.toList()));
            return ResponseEntity.ok(groupedTeam);

        } catch (IOException e) {
            log.error("Get Info Team By Manager Id Failure: {}", e.getMessage());
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
}
