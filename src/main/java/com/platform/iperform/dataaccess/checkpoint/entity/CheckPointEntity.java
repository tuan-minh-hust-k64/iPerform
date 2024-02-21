package com.platform.iperform.dataaccess.checkpoint.entity;

import com.platform.iperform.common.valueobject.BaseEntityAllowComment;
import com.platform.iperform.common.valueobject.CategoryCheckpoint;
import com.platform.iperform.common.valueobject.CheckPointStatus;
import com.platform.iperform.common.valueobject.RankingType;
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
@Table(name = "check_point")
public class CheckPointEntity extends BaseEntityAllowComment{
    private UUID userId;
    private ZonedDateTime createdAt;
    private String title;
    @Enumerated(EnumType.STRING)
    private CategoryCheckpoint category;
    @Enumerated(EnumType.STRING)
    private CheckPointStatus status;
    private ZonedDateTime lastUpdateAt;
    @Enumerated(EnumType.STRING)
    private RankingType ranking;

//    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
//    private List<CommentEntity> commentEntities;
    @OneToMany(mappedBy = "checkPoint", cascade = CascadeType.ALL)
    private List<CheckPointItemEntity> checkPointItemEntities;
    public CheckPointEntity(UUID id) {
        super.setId(id);
    }
}
