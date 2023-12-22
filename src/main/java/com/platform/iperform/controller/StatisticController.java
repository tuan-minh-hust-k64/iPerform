package com.platform.iperform.controller;

import com.platform.iperform.common.dto.request.CheckPointRequest;
import com.platform.iperform.common.dto.response.CheckPointResponse;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.service.CheckPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
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

    public StatisticController(FunctionHelper functionHelper, CheckPointService checkPointService) {
        this.functionHelper = functionHelper;
        this.checkPointService = checkPointService;
    }

    @GetMapping(value = "/my-team")
    public ResponseEntity<?> statisticMyTeam(@RequestParam String managerId, @RequestParam String title) {
        try {
            List<Map<String, Object>> result = functionHelper.getTeamByManagerId(managerId);
            result.forEach(item -> {
                CheckPointResponse statisticCheckPoint = checkPointService.findByUserIdAndTitle(CheckPointRequest.builder()
                        .userId(UUID.fromString(item.get("id").toString()))
                                .title(title)
                        .build());
                item.put("checkPointStatus", statisticCheckPoint.getData().getStatus());
            });
            return ResponseEntity.ok(result);

        } catch (IOException e) {
            log.error("Get Info Team By Manager Id Failure: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @GetMapping(value = "/check-point")
    ResponseEntity<?> statisticCheckPoint() {
        try {
            List<Map<String, Object>> result = functionHelper.getTeamByManagerId("2c7008db-1f20-4fbb-8d77-325431277220");
            List<Map<String, Object>> data = result.stream().map(item -> {
                CheckPointResponse statisticCheckPoint = checkPointService.findByUserIdAndTitle(CheckPointRequest.builder()
                        .userId(UUID.fromString(item.get("id").toString()))
                        .title(functionHelper.calculateQuarter())
                        .build());
                Map<String, Object> tempRep = (Map<String, Object>) item.get("teams");
                Map<String, Object> temp = new HashMap<>();
                temp.put("checkPointStatus", statisticCheckPoint.getData().getStatus());
                temp.put("id", item.get("id"));
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
}
