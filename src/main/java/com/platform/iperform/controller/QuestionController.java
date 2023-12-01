package com.platform.iperform.controller;

import com.platform.iperform.common.dto.QuestionRequest;
import com.platform.iperform.common.dto.QuestionResponse;
import com.platform.iperform.common.valueobject.QuestionStatus;
import com.platform.iperform.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;

@Controller
@RequestMapping(value = "/question")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
    @PostMapping(value = "/get-by-filter")
    public ResponseEntity<QuestionResponse> getQuestions(@RequestBody QuestionRequest questionRequest) {
        QuestionResponse result;
        if(questionRequest.getStatus() != null) {
            result = questionService.findByStatus(questionRequest);
        } else {
            result = questionService.findAll();
        }
        return ResponseEntity.ok(result);
    }
    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@RequestBody QuestionRequest questionRequest) {
        QuestionResponse result = questionService.createQuestion(questionRequest);
        return ResponseEntity.ok(result);
    }
    @PutMapping
    public ResponseEntity<QuestionResponse> updateQuestion(@RequestBody QuestionRequest questionRequest) {
        QuestionResponse result = questionService.updateQuestion(questionRequest);
        return ResponseEntity.ok(result);
    }
}
