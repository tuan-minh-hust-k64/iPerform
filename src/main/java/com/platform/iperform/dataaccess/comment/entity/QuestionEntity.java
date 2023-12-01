package com.platform.iperform.dataaccess.comment.entity;

import com.platform.iperform.common.valueobject.CommentType;
import com.platform.iperform.common.valueobject.QuestionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "question")
public class QuestionEntity {
    @Id
    private UUID id;
    private String content;
    @Enumerated(EnumType.STRING)
    private QuestionStatus status;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastUpdateAt;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<CommentEntity> commentEntities;
    public QuestionEntity(UUID id) {
        this.id = id;
    }
}
