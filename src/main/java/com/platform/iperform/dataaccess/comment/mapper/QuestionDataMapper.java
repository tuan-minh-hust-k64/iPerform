package com.platform.iperform.dataaccess.comment.mapper;

import com.platform.iperform.dataaccess.comment.entity.QuestionEntity;
import com.platform.iperform.model.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionDataMapper {
    public QuestionEntity questionToQuestionEntity(Question question) {
        return QuestionEntity.builder()
                .status(question.getStatus())
                .lastUpdateAt(question.getLastUpdateAt())
                .createdAt(question.getCreatedAt())
                .content(question.getContent())
                .build();
    }

    public Question questionEntityToQuestion(QuestionEntity questionEntity) {
        return Question.builder()
                .id(questionEntity.getId())
                .content(questionEntity.getContent())
                .lastUpdateAt(questionEntity.getLastUpdateAt())
                .createdAt(questionEntity.getCreatedAt())
                .build();
    }
}
