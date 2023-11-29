package com.platform.iperform.dataaccess.eks.entity;

import com.platform.iperform.common.valueobject.BaseEntityAllowComment;
import com.platform.iperform.common.valueobject.CheckInStatus;
import com.platform.iperform.dataaccess.comment.entity.CommentEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "check_in")
public class CheckInEntity extends BaseEntityAllowComment {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "e_id")
    private EksEntity eks;
    private ZonedDateTime createdAt;
    private String content;
    @Enumerated(EnumType.STRING)
    private CheckInStatus status;
    private String type;
    private ZonedDateTime lastUpdateAt;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<CommentEntity> commentEntities;
}
