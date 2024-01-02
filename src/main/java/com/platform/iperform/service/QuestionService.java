package com.platform.iperform.service;

import com.platform.iperform.common.dto.request.QuestionRequest;
import com.platform.iperform.common.dto.response.QuestionResponse;
import com.platform.iperform.common.exception.NotFoundException;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.dataaccess.comment.adapter.QuestionRepositoryImpl;
import com.platform.iperform.dataaccess.comment.entity.QuestionEntity;
import com.platform.iperform.dataaccess.comment.mapper.QuestionDataMapper;
import com.platform.iperform.model.Question;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class QuestionService {
    private final QuestionRepositoryImpl questionRepository;
    private final QuestionDataMapper questionDataMapper;
    private final FunctionHelper functionHelper;

    public QuestionService(QuestionRepositoryImpl questionRepository, QuestionDataMapper questionDataMapper, FunctionHelper functionHelper) {
        this.questionRepository = questionRepository;
        this.questionDataMapper = questionDataMapper;
        this.functionHelper = functionHelper;
    }
    public QuestionResponse createQuestion(QuestionRequest questionRequest) {
        List<Question> result = questionRepository.saveAll(questionRequest.getQuestions());
        return QuestionResponse.builder()
                .questions(result)
                .build();
    }
    public QuestionResponse updateQuestion(QuestionRequest questionRequest) {
        QuestionEntity questionEntity = questionRepository.findById(questionRequest.getQuestion().getId())
                .orElseThrow(() -> new NotFoundException("Not found question with id: " + questionRequest.getQuestion().getId()));
        questionEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        BeanUtils.copyProperties(
                questionRequest.getQuestion(),
                questionEntity,
                functionHelper.getNullPropertyNames(questionRequest.getQuestion())
        );
        Question result = questionDataMapper.questionEntityToQuestion(
                questionRepository.save(questionDataMapper.questionEntityToQuestion(questionEntity))
        );
        return QuestionResponse.builder()
                .questions(List.of(result))
                .build();
    }
    public QuestionResponse findByStatus(QuestionRequest questionRequest) {
        List<QuestionEntity> result = questionRepository.findByStatus(questionRequest.getStatus());
        return QuestionResponse.builder()
                .questions(result.stream().map(questionDataMapper::questionEntityToQuestion).toList())
                .build();
    }
    public QuestionResponse findAll() {
        List<QuestionEntity> result = questionRepository.findAll();
        return QuestionResponse.builder()
                .questions(result.stream().map(questionDataMapper::questionEntityToQuestion).toList())
                .build();
    }
}
