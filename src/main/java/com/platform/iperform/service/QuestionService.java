package com.platform.iperform.service;

import com.platform.iperform.common.dto.QuestionRequest;
import com.platform.iperform.common.dto.QuestionResponse;
import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.dataaccess.comment.adapter.QuestionRepositoryImpl;
import com.platform.iperform.dataaccess.comment.entity.QuestionEntity;
import com.platform.iperform.dataaccess.comment.exception.QuestionNotFoundException;
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
        QuestionEntity questionEntity = questionRepository.findById(questionRequest.getQuestions().get(0).getId())
                .orElseThrow(() -> new QuestionNotFoundException("Not found question with id: " + questionRequest.getQuestions().get(0).getId()));
        questionEntity.setLastUpdateAt(ZonedDateTime.now(ZoneId.of("UTC")));
        BeanUtils.copyProperties(
                questionRequest.getQuestions().get(0),
                questionEntity,
                functionHelper.getNullPropertyNames(questionRequest.getQuestions().get(0).getId())
        );
        Question result = questionDataMapper.questionEntityToQuestion(
                questionRepository.save(questionDataMapper.questionEntityToQuestion(questionEntity))
        );
        return QuestionResponse.builder()
                .questions(List.of(result))
                .build();
    }
}
