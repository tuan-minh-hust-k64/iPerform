package com.platform.iperform.controller;

import com.platform.iperform.common.dto.QuestionRequest;
import com.platform.iperform.common.dto.QuestionResponse;
import com.platform.iperform.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/question")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
    @PostMapping(value = "/create")
    public ResponseEntity<QuestionResponse> createQuestion(@RequestBody QuestionRequest questionRequest) {
        QuestionResponse result = questionService.createQuestion(questionRequest);
        return ResponseEntity.ok(result);
    }
    @PostMapping(value = "/update")
    public ResponseEntity<QuestionResponse> updateQuestion(@RequestBody QuestionRequest questionRequest) {
        QuestionResponse result = questionService.updateQuestion(questionRequest);
        return ResponseEntity.ok(result);
    }
}
