package com.platform.iperform.dataaccess.comment.adapter;

import com.platform.iperform.dataaccess.comment.entity.QuestionEntity;
import com.platform.iperform.dataaccess.comment.mapper.QuestionDataMapper;
import com.platform.iperform.dataaccess.comment.repository.QuestionJpaRepository;
import com.platform.iperform.model.Question;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class QuestionRepositoryImpl {
    private final QuestionJpaRepository questionJpaRepository;
    private final QuestionDataMapper questionDataMapper;

    public QuestionRepositoryImpl(QuestionJpaRepository questionJpaRepository, QuestionDataMapper questionDataMapper) {
        this.questionJpaRepository = questionJpaRepository;
        this.questionDataMapper = questionDataMapper;
    }

    public List<Question> saveAll(List<Question> questions) {
        return questionJpaRepository.saveAll(
                questions.stream().map(questionDataMapper::questionToQuestionEntity).toList()
        ).stream().map(questionDataMapper::questionEntityToQuestion).toList();
    }

    public QuestionEntity save(Question question) {
        return questionJpaRepository.save(questionDataMapper.questionToQuestionEntity(question));
    }

    public Optional<QuestionEntity> findById(UUID id) {
        return questionJpaRepository.findById(id);
    }
}
