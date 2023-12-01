package com.platform.iperform.dataaccess.comment.mapper;

import com.platform.iperform.common.utils.FunctionHelper;
import com.platform.iperform.common.valueobject.QuestionStatus;
import com.platform.iperform.dataaccess.comment.entity.QuestionEntity;
import com.platform.iperform.model.Question;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class QuestionDataMapper {
    private final FunctionHelper functionHelper;

    public QuestionDataMapper(FunctionHelper functionHelper) {
        this.functionHelper = functionHelper;
    }

    public QuestionEntity questionToQuestionEntity(Question question) {
        return QuestionEntity.builder()
                .id(question.getId() == null? UUID.randomUUID():question.getId())
                .status(question.getStatus() == null? QuestionStatus.ENABLE:question.getStatus())
                .lastUpdateAt(functionHelper.getZoneDateTime(question.getLastUpdateAt()))
                .createdAt(functionHelper.getZoneDateTime(question.getCreatedAt()))
                .content(question.getContent())
                .build();
    }

    public Question questionEntityToQuestion(QuestionEntity questionEntity) {
        return Question.builder()
                .id(questionEntity.getId())
                .content(questionEntity.getContent())
                .lastUpdateAt(questionEntity.getLastUpdateAt())
                .createdAt(questionEntity.getCreatedAt())
                .status(questionEntity.getStatus())
                .build();
    }
}
