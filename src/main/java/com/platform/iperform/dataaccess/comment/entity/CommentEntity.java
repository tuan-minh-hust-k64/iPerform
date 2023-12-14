package com.platform.iperform.dataaccess.comment.entity;

import com.platform.iperform.common.valueobject.BaseEntityAllowComment;
import com.platform.iperform.common.valueobject.CommentStatus;
import com.platform.iperform.common.valueobject.CommentType;
import com.platform.iperform.dataaccess.checkpoint.entity.CheckPointEntity;
import com.platform.iperform.dataaccess.checkpoint.entity.CheckPointItemEntity;
import com.platform.iperform.dataaccess.eks.entity.EksEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class CommentEntity {
    @Id
    private UUID id;
    private UUID userId;
    private UUID parentId;
    private String name;
    @Enumerated(EnumType.STRING)
    private CommentStatus status;
//    @JoinColumn(name = "parent_id")
//    @ManyToOne(cascade = CascadeType.ALL)
//    private BaseEntityAllowComment parent;
    @JoinColumn(name = "question_id")
    @OneToOne(cascade = CascadeType.ALL)
    private QuestionEntity question;
    private ZonedDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private CommentType type;
    private String content;
    private ZonedDateTime lastUpdateAt;

}
